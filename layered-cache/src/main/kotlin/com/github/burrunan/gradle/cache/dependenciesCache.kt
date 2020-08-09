package com.github.burrunan.gradle.cache

import com.github.burrunan.gradle.github.env.ActionsEnvironment
import com.github.burrunan.gradle.github.event.ActionsTrigger
import com.github.burrunan.gradle.github.event.cacheKey
import com.github.burrunan.gradle.github.suspendingStateVariable
import com.github.burrunan.gradle.hashFiles
import github.actions.core.debug

/**
 * Populate cache only when building a default branch, otherwise treat the cache as read-only.
 */
suspend fun dependenciesCache(
    name: String,
    trigger: ActionsTrigger,
    cacheLocation: List<String>,
    pathDependencies: List<String>,
): Cache {
    val defaultBranch = trigger.event.repository.default_branch
    val pkPrefix = trigger.cacheKey
    val cacheName = "dependencies-$name"

    // Avoid re-computing the hash for saving the cache
    val dependencyDeclarationHash = suspendingStateVariable(cacheName) {
        hashFiles(*pathDependencies.toTypedArray()).hash
    }
    debug { "$cacheName: dependencyDeclarationHash=${dependencyDeclarationHash.get()}" }
    val prefix = "dependencies-$name-${ActionsEnvironment.RUNNER_OS}"
    return LayeredCache(
        name = cacheName,
        baseline = prefix,
        primaryKey = "$prefix-$pkPrefix-${dependencyDeclarationHash.get()}",
        restoreKeys = listOf(
            "$prefix-$pkPrefix",
            "$prefix-$defaultBranch",
        ),
        paths = cacheLocation,
    )
}

suspend fun gradleDependenciesCache(trigger: ActionsTrigger, path: String, gradleDependenciesCacheKey: String): Cache =
    dependenciesCache(
        "gradle",
        trigger,
        cacheLocation = listOf(
            "~/.gradle/caches/modules-2",
            "!~/.gradle/caches/modules-2/gc.properties",
            "!~/.gradle/caches/modules-2/modules-2.lock",
        ),
        pathDependencies = listOf(
            "!$path/**/.gradle/",
            "$path/**/*.gradle.kts",
            "$path/**/gradle/dependency-locking/**",
            // We do not want .gradle folder, so we want to have at least one character before .gradle
            "$path/**/?*.gradle",
            "$path/**/*.properties",
        ) + gradleDependenciesCacheKey.split(Regex("[\r\n]+")).map {
            (if (it.startsWith("!")) "!" else "") +
                "$path/**/" + it.trim().trimStart('!')
        },
    )

suspend fun mavenDependenciesCache(trigger: ActionsTrigger, path: String): Cache =
    dependenciesCache(
        "maven",
        trigger,
        cacheLocation = listOf("~/.m2/repository"),
        pathDependencies = listOf(
            "$path/**/pom.xml",
        ),
    )

