package com.github.burrunan.gradle.cache

interface Cache {
    val name: String

    suspend fun save()
    suspend fun restore(): RestoreType
}

sealed class RestoreType {
    data class Exact(val path: String): RestoreType()
    data class Partial(val path: String): RestoreType()
    object None: RestoreType()
    object Unknown: RestoreType()
}
