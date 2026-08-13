/*
 * Copyright 2020 Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.burrunan.gradle.proxy

import actions.cache.RestoreType
import actions.cache.restoreAndLog
import actions.core.ActionFailedException
import actions.core.LogLevel
import actions.core.debug
import actions.core.warning
import actions.glob.removeFiles
import com.github.burrunan.gradle.cache.HttpException
import com.github.burrunan.gradle.cache.handle
import com.github.burrunan.wrappers.nodejs.discard
import com.github.burrunan.wrappers.nodejs.mkdir
import com.github.burrunan.wrappers.nodejs.pipeAndWait
import js.array.component1
import js.objects.unsafeJso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import node.fs.createReadStream
import node.fs.createWriteStream
import node.fs.stat
import node.http.IncomingMessage
import node.http.OutgoingHttpHeaders
import node.http.ServerResponse
import node.net.AddressInfo
import node.path.path
import node.process.process
import node.stream.pipeline
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Serves the Gradle HTTP build cache over the GitHub Actions cache.
 *
 * @param port port to listen on, or 0 to pick an ephemeral one. The port ends up in the init script
 * the proxy generates, and Gradle fingerprints init script content as a configuration input, so a
 * port that changes between runs invalidates the configuration cache every time. Builds that want to
 * reuse a configuration cache entry across runs need to pin it.
 */
class CacheProxy(private val port: Int = 0) {
    companion object {
        const val GHA_CACHE_URL = "GHA_CACHE_URL"
        private const val TEMP_DIR = ".cache-proxy"
        private val cacheVersion = "1-"

        /**
         * Keeps requests that share one temp file apart.
         *
         * Gradle stores and loads entries concurrently, and two tasks with the same inputs produce the
         * same id, so requests for one id do overlap. The locks live next to [TEMP_DIR] rather than in
         * the instance, since two proxies in one process address the same files.
         */
        private val entryLocks = EntryLocks()

        /**
         * Whether any request holds or waits for the temp file of [id].
         *
         * A request that ends without handing its id back leaves this answering `true` for the rest of
         * the run, and that id is then lost: [putEntry] answers every later PUT for it without storing
         * anything, and [getEntry] never answers at all.
         */
        internal fun holdsEntry(id: String): Boolean = entryLocks.holds(id)

        /**
         * Explains a failed bind, and names a remedy only when one exists.
         *
         * An ephemeral bind that fails leaves the user nothing to set, so mentioning the input there
         * would point them at the one thing that is not the cause.
         *
         * @param reason the message Node reported for the failure, for example `listen EADDRINUSE: ...`
         */
        internal fun bindFailureMessage(port: Int, reason: String?): String = when (port) {
            0 -> "Unable to start the remote build cache proxy: $reason"
            else -> "Unable to start the remote build cache proxy on port $port: $reason. " +
                "Set remote-build-cache-proxy-port to a free port, or to 0 for an ephemeral one"
        }
    }

    private var _cacheUrl: String? = null

    val cacheUrl: String? get() = _cacheUrl

    private val server = node.http.createServer<IncomingMessage, ServerResponse<*>> { req, res ->
        val query = node.url.parse(req.url!!, true)
        val path = query.pathname ?: ""
        res.handle {
            val id = path.removePrefix("/")
            when (req.method) {
                "GET" -> getEntry(id, res)
                "PUT" -> putEntry(id, req, res)
                else -> HttpException.notImplemented("Not implemented: ${req.method}")
            }
        }
    }

//    private val compression = jso<InternalCacheOptions> { compressionMethod = CompressionMethod.Gzip }

    private suspend fun putEntry(id: String, req: IncomingMessage, res: ServerResponse<*>) {
        val fileName = path.join(TEMP_DIR, "bc-$id")
        if (!entryLocks.beginStore(id)) {
            // A build cache key stands for the contents stored under it, so this PUT carries what the
            // store already under way is uploading, and the cache service rejects a second save of one
            // key anyway. Writing the temp file again would leave that store archiving a file that
            // changed underneath it.
            debug { "A store for build cache entry $id is already under way, dropping the duplicate" }
            req.discard()
            res.writeHead(200, "OK", undefined.unsafeCast<OutgoingHttpHeaders>())
            return
        }
        var storeStarted = false
        try {
            // pipeline closes the temp file and waits for the last byte to reach the disk, which is
            // what makes the archive below hold the whole payload
            pipeline(req, createWriteStream(fileName))
            res.writeHead(200, "OK", undefined.unsafeCast<OutgoingHttpHeaders>())
            // The store runs detached so the PUT can be answered before the upload finishes, which
            // leaves its failures with nowhere to go: without a handler they reach the uncaught-coroutine
            // handler and terminate the process along with the Gradle build the action is running.
            // A build cache entry that fails to store is not worth failing a build over.
            GlobalScope.launch(
                CoroutineExceptionHandler { _, e ->
                    warning("Unable to store build cache entry $id: ${e.stackTraceToString()}")
                },
            ) {
                try {
                    actions.cache.saveAndLog(listOf(fileName), id, cacheVersion, logLevel = LogLevel.DEBUG)
                } finally {
                    finishStore(id, fileName)
                }
            }
            storeStarted = true
        } finally {
            // The store owns the id from here on, and hands it back when it ends. A PUT that never got
            // that far has to hand it back itself, or every later request for the id waits forever.
            // Its temp file goes too: a body that stopped short would archive as a valid entry holding
            // a truncated payload.
            if (!storeStarted) {
                finishStore(id, fileName)
            }
        }
    }

    /** Removes the temp file the store of [id] wrote, and hands the id on to whoever waits for it. */
    private suspend fun finishStore(id: String, fileName: String) {
        try {
            removeFiles(listOf(fileName))
        } finally {
            entryLocks.endStore(id)
        }
    }

    private suspend fun getEntry(id: String, res: ServerResponse<*>) = entryLocks.withEntry(id) {
        val fileName = path.join(TEMP_DIR, "bc-$id")
        val restoreType = restoreAndLog(listOf(fileName), id, restoreKeys = listOf(), version = cacheVersion, logLevel = LogLevel.DEBUG)
        if (restoreType == RestoreType.None) {
            throw HttpException.notFound("No cache entry found for $id")
        }
        res.writeHead(
            200, "Ok",
            unsafeJso<OutgoingHttpHeaders> {
                contentLength = stat(fileName).size
            },
        )
        createReadStream(fileName).pipeAndWait(res)
    }

    private val pluginId = "com.github.burrunan.multi-cache"

    fun getMultiCacheConfiguration(
        multiCacheEnabled: Boolean = true,
        multiCacheVersion: String = "1.0",
        multiCacheRepository: String = "",
        multiCacheGroupIdFilter: String = "com[.]github[.]burrunan[.]multi-?cache",
        push: Boolean = true,
    ): String {
        val multiCacheGroupIdFilterEscaped = multiCacheGroupIdFilter.replace("\\", "\\\\")
        //language=Groovy
        return """
            def pluginId = 'com.github.burrunan.multi-cache'
            def multiCacheVersion = '1.0'
            def multiCacheGroupIdFilter = 'com[.]github[.]burrunan[.]multi-?cache'
            boolean multiCacheEnabled = $multiCacheEnabled
            String multiCacheRepository = '$multiCacheRepository'
            boolean gradle6Plus = org.gradle.util.GradleVersion.current() >= org.gradle.util.GradleVersion.version('6.0')
            // beforeSettings is Gradle 6.0+
            if (multiCacheEnabled && !gradle6Plus) {
                println("Multiple remote build caches ($pluginId) are supported in Gradle 6.0+ only")
                multiCacheEnabled = false
            }
            if (multiCacheEnabled) {
                beforeSettings { settings ->
                    def repos = settings.buildscript.repositories
                    if (multiCacheRepository != '') {
                        repos.add(
                            repos.maven {
                                url = multiCacheRepository
                                if ('$multiCacheGroupIdFilterEscaped' != '') {
                                    content {
                                        includeGroupByRegex('$multiCacheGroupIdFilterEscaped')
                                    }
                                }
                            }
                        )
                    } else if (repos.isEmpty()) {
                        repos.add(repos.gradlePluginPortal())
                    }
                    settings.buildscript.dependencies {
                        classpath("$pluginId:${pluginId}.gradle.plugin:$multiCacheVersion")
                    }
                }
            }

            settingsEvaluated { settings ->
                settings.buildCache {
                    boolean needMulticache = remote != null
                    if (needMulticache && !multiCacheEnabled) {
                        println("$pluginId is disabled")
                        return
                    }

                    local {
                        enabled = true
                        push = $push
                    }
                    if (needMulticache) {
                        settings.pluginManager.apply("$pluginId")
                        settings.multicache.push('base')
                    }
                    remote(HttpBuildCache) {
                        url = '$cacheUrl'
                        push = $push
                        // Build cache is located on localhost, so it is fine to use http protocol
                        if (gradle6Plus) {
                            allowInsecureProtocol = true
                        }
                    }
                    if (needMulticache) {
                        settings.multicache.pushAndConfigure('actions-cache') {
                            loadSequentiallyWriteConcurrently('actions-cache', 'base')
                        }
                    }
                }
            }
        """.trimIndent()
    }

    /**
     * Binds the proxy to [port] and publishes its URL through [cacheUrl] and `GHA_CACHE_URL`.
     *
     * @throws ActionFailedException the port is already taken, or the operating system refuses the bind
     */
    suspend fun start() {
        suspendCoroutine<Nothing?> { cont ->
            // Node reports a failed bind through an 'error' event rather than through the listen
            // callback, and an unobserved 'error' terminates the process
            val removeBindErrorHandler = server.errorEvent.addHandler { (error) ->
                cont.resumeWithException(ActionFailedException(bindFailureMessage(port, error.message)))
            }
            server.listen(port) {
                removeBindErrorHandler()
                cont.resume(null)
            }
        }

        mkdir(TEMP_DIR)
        val url = "http://localhost:${(server.address().unsafeCast<AddressInfo>()).port}/"
        _cacheUrl = url
        process.env[GHA_CACHE_URL] = url
    }

    fun stop() {
        server.close()
    }

    suspend inline operator fun <T> invoke(block: () -> T): T {
        start()
        try {
            return block()
        } finally {
            stop()
        }
    }
}
