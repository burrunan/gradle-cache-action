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
package com.github.burrunan.gradle.cache

import actions.cache.RestoreType
import actions.cache.restoreAndLog
import actions.cache.saveAndLog
import actions.core.debug
import actions.core.info
import actions.glob.removeFiles
import com.github.burrunan.formatBytes
import com.github.burrunan.gradle.github.stateVariable
import com.github.burrunan.gradle.github.toBoolean
import com.github.burrunan.gradle.github.toInt
import com.github.burrunan.hashing.HashContents
import com.github.burrunan.hashing.HashDetails
import com.github.burrunan.hashing.HashInfo
import com.github.burrunan.hashing.hashFilesDetailed
import com.github.burrunan.wrappers.nodejs.exists
import kotlin.math.absoluteValue

class DefaultCache(
    name: String,
    val primaryKey: String,
    private val restoreKeys: List<String> = listOf(),
    private val paths: List<String>,
    private val readOnlyMessage: String? = null,
    stateKey: String = "",
    private val skipRestoreIfPathExists: String? = null
) : Cache {
    @Suppress("CanBePrimaryConstructorProperty")
    override val name: String = name

    private val version = "1-"

    private val cacheInfo =
        MetadataFile("$name-info", HashInfo.serializer())
    private val cacheContents =
        MetadataFile("$name-contents", HashContents.serializer())
    private val saveRestorePaths = paths + cacheInfo.cachedName + cacheContents.cachedName

    private val isExactMatch = stateVariable("${name}_${stateKey}_exact").toBoolean()
    private val isSkipped = stateVariable("${name}_${stateKey}_skip").toBoolean()
    private val restoredKeyIndex = stateVariable("${name}_${stateKey}_key").toInt(-1)

    private val restoredKey: String?
        get() = when {
            isExactMatch.get() -> primaryKey
            restoredKeyIndex.get() >= 0 -> restoreKeys[restoredKeyIndex.get()]
            else -> null
        }

    private var details: HashDetails? = null

    suspend fun info(): HashInfo? {
        details?.info?.let { return it }
        restoredKey?.let { cacheContents.prepare(it) }
        if (!isExactMatch.get() && restoredKeyIndex.get() == -1) {
            // Cache was not restored => no information known
            return null
        }
        return cacheInfo.decode()
    }

    suspend fun contents(): HashContents? {
        details?.contents?.let { return it }
        restoredKey?.let { cacheContents.prepare(it) }
        if (!isExactMatch.get() && restoredKeyIndex.get() == -1) {
            // Cache was not restored => no information known
            return null
        }
        return cacheContents.decode()
    }

    override suspend fun restore(): RestoreType {
        skipRestoreIfPathExists?.let {
            if (exists(it)) {
                debug { "$name: $it already exists, so the cache restore and upload will be skipped" }
                isSkipped.set(true)
            }
        }
        debug { "$name: restoring $primaryKey, $restoreKeys, $saveRestorePaths" }
        return restoreAndLog(saveRestorePaths, primaryKey, restoreKeys, version = version).also {
            isExactMatch.set(it is RestoreType.Exact)
            restoredKeyIndex.set(
                when (it) {
                    is RestoreType.Partial -> restoreKeys.indexOfFirst { key -> it.path.startsWith(key) }
                    else -> -1
                },
            )
            debug { "$name: restore type $it, ${isExactMatch.get()}, ${restoredKeyIndex.get()}" }

            restoredKey?.let { key ->
                cacheInfo.restore(key)
                cacheContents.restore(key)
            }
        }
    }

    override suspend fun save() {
        debug { "$name: saving ${isExactMatch.get()} ${restoredKeyIndex.get()} $primaryKey, $restoreKeys, $saveRestorePaths" }
        if (isSkipped.get()) {
            debug { "$name: cache save skipped" }
            return
        }
        if (isExactMatch.get()) {
            info("$name loaded from exact match, no need to update the cache entry")
            return
        }
        readOnlyMessage?.let {
            info("$name is configured as read-only: $it")
            return
        }

        restoredKey?.let { key ->
            cacheInfo.prepare(key)
            cacheContents.prepare(key)
        }

        val oldHash = info()

        val newHash = hashFilesDetailed(*paths.toTypedArray())
        details = newHash

        if (newHash.contents.files.isEmpty()) {
            info("$name: no files to cache => won't upload empty cache")
            return
        }
        if (oldHash != null) {
            info("$name: comparing modifications of the cache contents")
            if (newHash.info.hash == oldHash.hash) {
                info("$name: contents did not change => no need to upload it")
                return
            }
            val delta = newHash.info.totalBytes - oldHash.totalBytes
            info("$name: hash content differs (${delta.absoluteValue} bytes ${if (delta >= 0) "increase" else "decrease"})")
        }
        info("$name: uploading ${newHash.info.totalBytes.formatBytes()}, ${newHash.contents.files.size} files as $primaryKey")
        cacheInfo.encode(newHash.info)
        cacheContents.encode(newHash.contents)
        saveAndLog(saveRestorePaths, primaryKey, version = version)
    }
}
