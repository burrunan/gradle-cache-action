package com.github.burrunan.gradle.cache

import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class CompositeCache(
    override val name: String,
    private val caches: List<Cache>,
) : Cache {
    override suspend fun save() = supervisorScope {
        for (cache in caches) {
            launch {
                cache.save()
            }
        }
    }

    override suspend fun restore(): RestoreType = supervisorScope {
        for (cache in caches) {
            launch {
                cache.restore()
            }
        }
        RestoreType.Unknown
    }
}
