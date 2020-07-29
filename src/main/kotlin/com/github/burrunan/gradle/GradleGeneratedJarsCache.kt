package com.github.burrunan.gradle

import com.github.burrunan.gradle.cache.ImmutableCache

class GradleGeneratedJarsCache(gradleVersion: String) : ImmutableCache("gradle-generated-jars") {
    override val primaryKey = "generated-gradle-jars-$gradleVersion"
    override val paths = arrayOf(
        "~/*.*/generated-gradle-jars",
        "!~/*.*/generated-gradle-jars/*.lock"
    )
}
