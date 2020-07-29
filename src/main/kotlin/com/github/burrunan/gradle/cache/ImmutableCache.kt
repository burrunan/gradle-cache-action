package com.github.burrunan.gradle.cache

import com.github.burrunan.gradle.github.stateVariable
import com.github.burrunan.gradle.github.toBoolean
import com.github.burrunan.gradle.hashFiles
import github.actions.cache.restoreAndLog
import github.actions.cache.saveAndLog
import github.actions.core.info

abstract class ImmutableCache(
    name: String
) : Cache {
    @Suppress("CanBePrimaryConstructorProperty")
    override val name: String = name

    private val isExactMatch = stateVariable("${name}_exact").toBoolean()

    override suspend fun restore(): RestoreType {
        info("Restoring $name")
        return restoreAndLog(paths, primaryKey).also {
            isExactMatch.set(it == RestoreType.EXACT)
            hashFiles(*paths)
        }
    }

    override suspend fun save() {
        hashFiles(*paths)
        if (isExactMatch.get()) {
            info("$name loaded from exact match, so avoid storing it")
            return
        }
        info("Saving $name")
        saveAndLog(paths, primaryKey)
    }
}
