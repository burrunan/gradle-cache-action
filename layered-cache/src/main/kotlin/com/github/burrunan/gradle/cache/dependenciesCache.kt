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

import actions.core.ActionsEnvironment
import actions.core.debug
import com.github.burrunan.gradle.GradleCacheAction
import com.github.burrunan.gradle.github.suspendingStateVariable
import com.github.burrunan.hashing.hashFiles
import octokit.ActionsTrigger

/**
 * Populate cache only when building a default branch, otherwise treat the cache as read-only.
 */
suspend fun dependenciesCache(
    name: String,
    trigger: ActionsTrigger,
    cacheLocation: List<String>,
    pathDependencies: List<String>,
): Cache {
    val defaultBranch = GradleCacheAction.DEFAULT_BRANCH_VAR
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
            "$prefix-master",
            "$prefix-main",
        ),
        paths = cacheLocation,
    )
}

suspend fun gradleDependenciesCache(trigger: ActionsTrigger, path: String, gradleDependenciesCacheKey: List<String>): Cache =
    dependenciesCache(
        "gradle",
        trigger,
        cacheLocation = listOf(
            "~/.gradle/caches/modules-2/*",
            "!~/.gradle/caches/modules-2/gc.properties",
            "!~/.gradle/caches/modules-2/modules-2.lock",
        ),
        pathDependencies = listOf(
            "$path/**/*.gradle",
            "$path/**/*.gradle.kts",
            "$path/**/gradle/dependency-locking/**",
            "$path/**/*.properties",
        ) + gradleDependenciesCacheKey.map {
                (if (it.startsWith("!")) "!" else "") +
                    "$path/**/" + it.trim().trimStart('!')
            } +
            // Excludes must go the last so they win
            listOf("!$path/**/.gradle/"),
    )

suspend fun mavenDependenciesCache(trigger: ActionsTrigger, path: String, mavenLocalIgnorePaths: List<String>): Cache =
    dependenciesCache(
        "maven",
        trigger,
        cacheLocation = listOf("~/.m2/repository") +
            mavenLocalIgnorePaths.map { "!~/.m2/repository/$it" },
        pathDependencies = listOf(
            "$path/**/pom.xml",
        ),
    )

