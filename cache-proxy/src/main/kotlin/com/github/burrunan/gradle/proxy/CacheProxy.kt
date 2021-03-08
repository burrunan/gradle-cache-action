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

import actions.core.debug
import actions.glob.removeFiles
import com.github.burrunan.gradle.cache.*
import com.github.burrunan.wrappers.nodejs.mkdir
import com.github.burrunan.wrappers.nodejs.use
import fs.createReadStream
import fs.createWriteStream
import http.IncomingMessage
import http.ServerResponse
import kotlinext.js.jsObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import path.path
import process
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CacheProxy {
    companion object {
        const val GHA_CACHE_URL = "GHA_CACHE_URL"
        private const val TEMP_DIR = ".cache-proxy"
    }

    private var _cacheUrl: String? = null

    val cacheUrl: String? get() = _cacheUrl

    private val server = http.createServer { req, res ->
        val query = url.parse(req.url, true)
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

    private val compression = jsObject<InternalCacheOptions> { compressionMethod = CompressionMethod.Gzip }

    private suspend fun putEntry(id: String, req: IncomingMessage, res: ServerResponse) {
        val fileName = path.join(TEMP_DIR, "bc-$id")
        try {
            req.use {
                it.pipe(createWriteStream(fileName))
            }
            res.writeHead(200, "OK")
        } finally {
            GlobalScope.launch {
                try {
                    val cacheIdRequest = reserveCache(id, arrayOf(id), compression)
                    val cacheId = cacheIdRequest.await()
                    saveCache(cacheId, fileName).await()
                } finally {
                    removeFiles(listOf(fileName))

                }
            }
        }
    }

    private suspend fun getEntry(id: String, res: ServerResponse) {
        val cacheEntry = getCacheEntry(arrayOf(id), arrayOf(id), compression).await()
            ?: throw HttpException.notFound("No cache entry found for $id")
        val archiveLocation = cacheEntry.archiveLocation
            ?: throw HttpException.notFound("No archive location for $id")
        val fileName = path.join(TEMP_DIR, "dl-$id")
        debug { "Found ${cacheEntry.cacheKey}, ${cacheEntry.scope} $archiveLocation" }
        try {
            downloadCache(archiveLocation, fileName).await()
            res.writeHead(
                200, "Ok",
                jsObject {
                    this["content-length"] = fs2.promises.stat(fileName).size
                },
            )
            createReadStream(fileName).use {
                it.pipe(res)
            }
        } finally {
            removeFiles(listOf(fileName))
        }
    }

    private val pluginId = "com.github.burrunan.multi-cache"

    fun getMultiCacheConfiguration(
        multiCacheEnabled: Boolean = true,
        multiCacheVersion: String = "1.0",
        multiCacheRepository: String = "",
        multiCacheGroupIdFilter: String = "com[.]github[.]burrunan[.]multi-?cache"
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
                    }
                    if (needMulticache) {
                        settings.pluginManager.apply("$pluginId")
                        settings.multicache.push('base')
                    }
                    remote(HttpBuildCache) {
                        url = '$cacheUrl'
                        push = true
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
        val url = "http://localhost:${server.address().port}/"
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
