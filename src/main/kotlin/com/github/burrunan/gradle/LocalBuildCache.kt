package com.github.burrunan.gradle

import com.github.burrunan.gradle.cache.ImmutableCache
import github.actions.cache.restoreCache
import github.actions.core.info
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class LocalBuildCache(
    val refName: String,
    val gradleVersion: String,
    val allFiles: String
) : ImmutableCache("build-cache") {
    override val primaryKey: String
        get() = TODO("Not yet implemented")
    override val paths: Array<String>
        get() = TODO("Not yet implemented")

    suspend fun restore2() {
        info("Restoring build-cache")
        val items = "0123456789abcdef"
        supervisorScope {
            for (item in items) {
                launch {
                    val restoreKey = restoreCache(
                        arrayOf(
                            "~/build-cache-*/$item*",
                            "!~/build-cache-*/*.lock"
                        ),
                        "gradle-build-cache-$gradleVersion-$allFiles",
                        arrayOf(
                            "gradle-build-cache-$gradleVersion"
                        )
                    ).await()
                    info("Restored from $restoreKey")
                }
            }
        }

    }
}
