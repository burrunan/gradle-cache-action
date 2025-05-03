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
import actions.glob.removeFiles
import com.github.burrunan.gradle.cache.HttpException
import com.github.burrunan.gradle.cache.handle
import com.github.burrunan.wrappers.nodejs.mkdir
import com.github.burrunan.wrappers.nodejs.pipeAndWait
import js.objects.unsafeJso
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CacheProxy {
    companion object {
        const val GHA_CACHE_URL = "GHA_CACHE_URL"
        private const val TEMP_DIR = ".cache-proxy"
        private val cacheVersion = "1-"
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
        try {
            req.pipeAndWait(createWriteStream(fileName))
            res.writeHead(200, "OK", undefined.unsafeCast<OutgoingHttpHeaders>())
        } finally {
            GlobalScope.launch {
                try {
                    actions.cache.saveAndLog(listOf(fileName), id, cacheVersion)
                } finally {
                    removeFiles(listOf(fileName))
                }
            }
        }
    }

    private suspend fun getEntry(id: String, res: ServerResponse<*>) {
        val fileName = path.join(TEMP_DIR, "bc-$id")
        val restoreType = restoreAndLog(listOf(fileName), id, restoreKeys = listOf(), version = cacheVersion)
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

    suspend fun start() {
        suspendCoroutine<Nothing?> { cont ->
            server.listen(0) {
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
