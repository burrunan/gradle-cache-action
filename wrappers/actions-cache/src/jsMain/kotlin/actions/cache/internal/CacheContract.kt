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
@file:JsModule("@actions/cache/lib/internal/constants")
package actions.cache.internal

import actions.cache.HttpClientError

external interface GetCacheParams {
    val keys: String
    val version: String
}

external interface ReserveCacheRequest {
    val key: String
    val version: String?
    var cacheSize: Number?
}

external interface ReserveCacheResponse {
    var cacheId: Number
}

external interface ArtifactCacheEntry {
    var cacheKey: String?
    var scope: String?
    var creationTime: String?
    var archiveLocation: String?
}

external interface CommitCacheRequest {
    val size: Number
}

external interface InternalCacheOptions {
    var compressionMethod: CompressionMethod?
    var cacheSize: Number?
}

external enum class CompressionMethod {
    Gzip,

    // Long range mode was added to zstd in v1.3.2.
    // This enum is for earlier version of zstd that does not have --long support
    ZstdWithoutLong,
    Zstd
}

external interface ITypedResponse<T> {
    var statusCode: Number
    var result: T?
    var headers: Any
}

external interface ITypedResponseWithError<T>: ITypedResponse<T> {
    val error: HttpClientError?
}
