package com.github.burrunan.gradle.cache

interface Cache {
    val name: String
    val primaryKey: String
    val paths: Array<String>

    suspend fun save()
    suspend fun restore(): RestoreType
}

enum class RestoreType {
    EXACT, PARTIAL, NONE
}
