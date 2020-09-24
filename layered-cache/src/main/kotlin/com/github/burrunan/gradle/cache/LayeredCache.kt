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
import actions.core.ActionFailedException
import actions.core.debug
import actions.core.info
import actions.glob.removeFiles
import com.github.burrunan.formatBytes
import com.github.burrunan.gradle.github.stateVariable
import com.github.burrunan.gradle.github.toBoolean
import com.github.burrunan.hashing.*
import kotlinx.serialization.Serializable

@Serializable
class CacheLayers(
    val layers: List<CacheLayer>,
    val deletedFiles: List<String>,
)

@Serializable
class CacheLayer(
    // format is $version-...
    val primaryKey: String,
    val paths: List<String>,
)

class LayeredCache(
    override val name: String,
    private val baseline: String,
    val maxLayers: Int = 5,
    private val primaryKey: String,
    private val restoreKeys: List<String> = listOf(),
    private val paths: List<String>,
) : Cache {
    private val version = "1"

    private val layers = MetadataFile("layer-$name", CacheLayers.serializer())

    private val isExactMatch = stateVariable("${name}_exact").toBoolean()

    private val index = DefaultCache(
        name = "$version-index-$name",
        primaryKey = "$version-index-$primaryKey",
        restoreKeys = restoreKeys.map { "$version-index-$it" },
        paths = listOf(layers.cachedName),
    )

    private fun CacheLayer.toCache(stateKey: String) =
        DefaultCache(
            name = name,
            stateKey = stateKey,
            primaryKey = primaryKey,
            restoreKeys = if (paths.isNotEmpty()) listOf() else restoreKeys.map { "$version-$it" },
            paths = this@toCache.paths.ifEmpty { this@LayeredCache.paths },
        )

    private fun Diff.toLayer(): CacheLayer {
        // @actions/cache treats "paths" as a part of the cache key, so "delta-" is not important here for correctness
        // delta- is here for readability
        return CacheLayer(
            primaryKey = "$version-delta-$primaryKey",
            paths = updatedFiles,
        )
    }

    override suspend fun restore(): RestoreType {
        val indexRestoreType = index.restore()
        if (indexRestoreType == RestoreType.None) {
            return RestoreType.None
        }
        val cacheIndex = layers.decode() ?: throw ActionFailedException("${layers.cachedName} is not found")

        var restoreType: RestoreType = when (indexRestoreType) {
            is RestoreType.Exact -> RestoreType.Exact(indexRestoreType.path.removePrefix("$version-index-"))
            is RestoreType.Partial -> RestoreType.Partial(indexRestoreType.path.removePrefix("$version-index-"))
            else -> indexRestoreType
        }

        info(
            cacheIndex.layers.joinToString(", ", "$name: ${cacheIndex.layers.size} layers. ") {
                if (it.paths.isEmpty()) it.primaryKey else "${it.primaryKey} (${it.paths.size} files)"
            },
        )

        // Restore layers one by one, so newer layers can overwrite the old files
        for ((index, layer) in cacheIndex.layers.withIndex()) {
            val cache = layer.toCache(index.toString())
            val restore = cache.restore()
            if (restore !is RestoreType.Exact) {
                restoreType = RestoreType.Unknown
            }
            debug { "$name: layer $index, restore=$restore" }
        }
        removeFiles(cacheIndex.deletedFiles)
        isExactMatch.set(restoreType is RestoreType.Exact)
        return restoreType
    }

    override suspend fun save() {
        if (isExactMatch.get()) {
            info("$name loaded from exact match, no need to update the cache entry")
            return
        }

        val cacheIndex = layers.decode(warnOnMissing = false)
        val isBaseline = primaryKey.startsWith(baseline)
        if (cacheIndex == null) {
            if (!isBaseline) {
                info("$name: old contents is not found, and the current cache $primaryKey does not start with $baseline, so cache saving can't be done")
                return
            }
            saveSingleLayerCache()
            return
        }

        // PR: keep all baseline layers, add one new for PR
        // branch:

        val caches = cacheIndex.layers
            .mapIndexed { index, cacheLayer -> cacheLayer.toCache(index.toString()) }
        val oldContents = caches.associateWith { it.contents() }

        if (isBaseline) {
            val missing = oldContents.mapNotNull { (cache, contents) ->
                if (contents == null) cache.primaryKey else null
            }

            if (missing.isNotEmpty()) {
                info("$name: there are missing layers: $missing")
                saveSingleLayerCache()
                return
            }
            if (cacheIndex.layers.size > maxLayers) {
                info("$name: ${cacheIndex.layers.size} layers reached, will create new snapshot")
                saveSingleLayerCache()
                return
            }
        }

        // non-baseline

        if (!isBaseline) {
            val firstLayer = cacheIndex.layers.firstOrNull()
            val firstBaseline = caches.firstOrNull { it.name.startsWith(baseline) }?.name
            if (firstLayer?.primaryKey?.startsWith("$version-$baseline") != true) {
                info("$name: the first baseline is not found, and the current cache $primaryKey does not start with $version-$baseline, so cache saving can't be done")
                return
            }
            if (oldContents.values.firstOrNull() == null) {
                info("$name: the first baseline $firstBaseline was not received, and the current cache $primaryKey does not start with $baseline, so cache saving can't be done")
                return
            }
        }

        val newContents = hashFilesDetailed(*paths.toTypedArray())

        val oldFiles = mutableMapOf<String, FileDetails>()
        val reusedFiles = mutableMapOf<String, FileDetails>()
        val deletedFiles = mutableSetOf<String>()
        val newLayers = mutableListOf<CacheLayer>()
        val layerInfo = mutableListOf<String>()
        for ((layer, contents) in cacheIndex.layers.zip(oldContents.values)) {
            if (contents == null) {
                info("$name: unknown contents for layer ${layer.primaryKey}")
                continue
            }
            var helpfulBytes = 0L
            var wastedBytes = 0L
            val helpfulLayerFiles = mutableMapOf<String, FileDetails>()
            val deletedLayerFiles = mutableListOf<String>()
            for ((file, details) in contents.files) {
                val newDetails = newContents.contents.files[file]
                if (details.hash == newDetails?.hash && file !in reusedFiles) {
                    // same file => previous layer is helpful
                    helpfulBytes += details.fileSize
                    helpfulLayerFiles[file] = details
                } else {
                    // file is different or removed => previous layer is not useful
                    if (newDetails == null) {
                        deletedLayerFiles.add(file)
                    }
                    wastedBytes += details.fileSize
                }
            }
            if (wastedBytes >= helpfulBytes) {
                // Too much waste => remove the layer
                info("$name: layer ${layer.primaryKey} has too much waste (${wastedBytes.formatBytes()} > ${helpfulBytes.formatBytes()}), so the layer will be skipped")
                continue
            }
            oldFiles += contents.files
            reusedFiles += helpfulLayerFiles
            deletedFiles += deletedLayerFiles
            newLayers += layer
            layerInfo += "${layer.primaryKey} ${(helpfulBytes + wastedBytes).formatBytes()} total (${contents.files.size} files), ${wastedBytes.formatBytes()} outdated"
        }

        if (!isBaseline && newLayers.isEmpty()) {
            info("$name: at least one layer from the default branch is needed. The new contents is ${newContents.info.totalBytes.formatBytes()} (${newContents.info.totalFiles} files)")
            return
        }

        val diff = diff(HashContents(oldFiles), newContents.contents)
        if (diff.messages.isNotEmpty()) {
            info("$name: cache contents is changed: ${diff.summary}")
        }

        val layer = diff.toLayer()
        val cache = layer.toCache("newlayer")
        // TODO: reuse HashDetails when saving cache
        cache.save()

        newLayers += layer
        layerInfo += "${layer.primaryKey} ${diff.totalUpdated.formatBytes()} total (${diff.updatedFiles.size} files), ${diff.totalDeleted.formatBytes()} deleted (${diff.deletedFiles.size} files)"

        info(layerInfo.joinToString("; ", "$name: ${newLayers.size} layers. "))

        layers.encode(CacheLayers(newLayers, deletedFiles = deletedFiles.toList()))
        // Save the index
        index.save()
    }

    private suspend fun saveSingleLayerCache() {
        info("$name: creating single-layer cache image")
        val layer = CacheLayer(
            primaryKey = "$version-$primaryKey",
            paths = listOf(),
        )
        val cache = layer.toCache("single-layer")
        cache.save()
        if (cache.info()?.totalFiles == 0) {
            // cache is empty => skip creating empty file
            return
        }
        layers.encode(CacheLayers(listOf(layer), deletedFiles = listOf()))
        index.save()
    }
}
