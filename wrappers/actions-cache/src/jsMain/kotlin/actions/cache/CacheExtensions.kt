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
package actions.cache

import actions.core.LogLevel
import actions.core.info
import actions.core.log
import actions.core.warning

suspend fun restoreAndLog(
    paths: List<String>, primaryKey: String,
    restoreKeys: List<String> = listOf(),
    version: String,
    logLevel: LogLevel = LogLevel.INFO,
): RestoreType {
    val result = try {
        when {
            restoreKeys.isEmpty() -> restoreCache(paths.toTypedArray(), version + primaryKey)
            else -> restoreCache(
                paths.toTypedArray(),
                version + primaryKey,
                restoreKeys.map { version + it }.toTypedArray(),
            )
        }
    } catch (t: Throwable) {
        when (t.asDynamic().name) {
            "ValidationError" -> throw t
            else -> {
                warning("Error while loading $primaryKey: ${t.message}")
                return RestoreType.None
            }
        }
    }
    result?.removePrefix(version)?.let {
        return if (it.endsWith(primaryKey)) RestoreType.Exact(it) else RestoreType.Partial(it)
    }
    log(logLevel) { "Cache was not found for version=$version, primaryKey=$primaryKey, restore keys=${restoreKeys.joinToString(", ")}" }
    return RestoreType.None
}

suspend fun saveAndLog(
    paths: List<String>,
    key: String,
    version: String,
    logLevel: LogLevel = LogLevel.INFO,
) {
    try {
        saveCache(paths.toTypedArray(), version + key)
    } catch (t: Throwable) {
        // TODO: propagate error
        when (t.asDynamic().name) {
            "ValidationError" -> throw t
            "ReserveCacheError" -> info(t.message ?: "Unknown ReserveCacheError")
            else -> when {
                t.message?.contains("Cache already exists") == true ->
                    log(logLevel) { "Error while uploading $key: ${t.message}" }
                else ->
                    warning("Error while uploading $key: ${t.message}")
            }
        }
    }
}
