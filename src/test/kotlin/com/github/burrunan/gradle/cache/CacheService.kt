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
 *
 */
package com.github.burrunan.gradle.cache

import com.github.burrunan.gradle.exists
import com.github.burrunan.gradle.readJson
import com.github.burrunan.gradle.readToBuffer
import com.github.burrunan.gradle.suspendWithCallback
import github.actions.core.debug
import http.IncomingMessage
import http.ServerResponse
import kotlinext.js.jsObject
import process
import url.UrlWithParsedQuery
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CacheService {
    companion object {
        const val ARCHIVE_DOWNLOAD_URL = "_apis/artifactcache/get"
    }

    private val storage = CacheStorage()

    private val server = http.createServer { req, res ->
        val query = url.parse(req.url, true)
        val path = query.pathname ?: ""
        res.handle {
            when {
                path == "/_apis/artifactcache/caches" && req.method == "POST" ->
                    reserveCache(req, res)
                path == "/_apis/artifactcache/cache" && req.method == "GET" ->
                    getCache(query, res)
                path.endsWith(ARCHIVE_DOWNLOAD_URL) && req.method == "GET" ->
                    getContents(query, res)
                path.startsWith("/_apis/artifactcache/caches/") ->
                    cacheOp(path.substringAfter("/_apis/artifactcache/caches/").toInt(), req, res)
                else -> HttpException.notImplemented("Path: $path")
            }
        }
    }

    private fun getContents(query: UrlWithParsedQuery, res: ServerResponse) {
        val key = query.query["key"] as String
        val entry = storage.getValue(key)
        res.writeHead(
            200, "Ok",
            jsObject {
                this["content-length"] = entry.value.length
            },
        )
        res.write(entry.value)
    }

    private suspend fun cacheOp(cacheId: Number, req: IncomingMessage, res: ServerResponse) = when (req.method) {
        "PATCH" -> uploadCache(cacheId, req, res)
        "POST" -> commitCache(cacheId, req, res)
        else -> throw HttpException.notImplemented("Unknown method: ${req.method}")
    }

    private fun getCache(query: UrlWithParsedQuery, res: ServerResponse) {
        val request = query.query.unsafeCast<GetCacheParams>()
        var resultKey: String? = null
        var resultEntry: CacheEntry? = null
        for ((index, key) in request.keys.split(',').withIndex()) {
            if (index == 0) {
                val entry = storage[key] ?: continue
                if (entry.version != request.version) {
                    debug("Entry version differs for key $key. Requested: ${request.version}, actual: ${entry.version}")
                }
                resultKey = key
                resultEntry = entry
                break
            }
            val entry = storage.find(key, request.version) ?: continue
            resultKey = entry.key
            resultEntry = entry.value
        }
        if (resultKey == null) {
            throw HttpException.noContent("No entries found")
        }

        res.writeHead(200, "Ok")

        res.write(
            JSON.stringify(
                jsObject<ArtifactCacheEntry> {
                    cacheKey = resultKey
                    scope = "refs/origin/main"
                    creationTime = resultEntry?.creationTime?.toString()
                    archiveLocation =
                        "${process.env["ACTIONS_CACHE_URL"]}$ARCHIVE_DOWNLOAD_URL?key=$resultKey".takeIf { resultEntry != null }
                },
            ),
        )
    }

    private suspend fun uploadCache(cacheId: Number, req: IncomingMessage, res: ServerResponse) {
        val contentRange = req.headers.asDynamic()["content-range"] as String
        val (_, start, end) = contentRange.match("bytes (\\d+)-(\\d+)") ?: arrayOf("", "", "")
        if (start.isEmpty()) {
            throw HttpException.notImplemented("Unknown content-range: $contentRange")
        }
        storage.update(cacheId, start.toInt(), end.toInt(), req.readToBuffer())
        res.writeHead(200, "OK")
    }

    private suspend fun commitCache(cacheId: Number, req: IncomingMessage, res: ServerResponse) {
        storage.commitCache(cacheId, req.readJson<CommitCacheRequest>().size)
        res.writeHead(200, "OK")
    }

    private suspend fun reserveCache(req: IncomingMessage, res: ServerResponse) {
        if (req.method != "POST") {
            throw HttpException.badRequest("Expecting POST method, got ${req.method}")
        }
        val request = req.readJson<ReserveCacheRequest>()

        val cacheId = storage.reserveCache(request.key, request.version!!)
            ?: throw HttpException.badRequest("Cache entry already exists")
        res.writeHead(200, "Reserve Cache OK")
        res.write(
            JSON.stringify(
                jsObject<ReserveCacheResponse> {
                    this.cacheId = cacheId
                },
            ),
        )
    }

    suspend inline operator fun <T> invoke(block: () -> T): T {
        start()
        try {
            return block()
        } finally {
            stop()
        }
    }

    suspend fun start() {
        suspendCoroutine<Nothing?> { cont ->
            server.listen(0) {
                cont.resume(null)
            }
        }

        val runnerTemp = "runner_temp"
        if (!exists(runnerTemp)) {
            fs2.promises.mkdir(runnerTemp)
        }

        process.env["ACTIONS_RUNTIME_TOKEN"] = "42"
        process.env["RUNNER_TEMP"] = process.cwd() + "/" + runnerTemp
        process.env["ACTIONS_CACHE_URL"] = "http://localhost:${server.address().port}/"
    }

    suspend fun stop() {
        suspendWithCallback { server.close(it) }
    }
}
