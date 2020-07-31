package com.github.burrunan.gradle

import com.github.burrunan.gradle.cache.Cache
import com.github.burrunan.gradle.cache.DefaultCache

fun gradleGeneratedJarsCache(gradleVersion: String): Cache =
    DefaultCache(
        name = "gradle-generated-jars",
        primaryKey = "generated-gradle-jars-$gradleVersion",
        paths = listOf(
            "~/.gradle/caches/*.*/generated-gradle-jars",
            "!~/.gradle/caches/*.*/generated-gradle-jars/*.lock"
        ),
    )
