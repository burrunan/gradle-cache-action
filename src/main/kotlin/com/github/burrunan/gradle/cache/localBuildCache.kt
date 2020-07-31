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
 *
 */
package com.github.burrunan.gradle.cache

import com.github.burrunan.gradle.github.event.ActionsTrigger
import com.github.burrunan.gradle.github.event.cacheKey

fun localBuildCache(jobId: String, trigger: ActionsTrigger, gradleVersion: String, treeId: String): Cache {
    val buildCacheLocation = "~/.gradle/caches/build-cache-1"
    val defaultBranch = trigger.event.repository.default_branch
    val pkPrefix = trigger.cacheKey

    val restoreKeys = when (trigger) {
        is ActionsTrigger.PullRequest -> arrayOf(
            pkPrefix,
            trigger.event.pull_request.base.ref.removePrefix("refs/heads/"),
            defaultBranch,
        )
        is ActionsTrigger.BranchPush -> arrayOf(
            pkPrefix,
            defaultBranch,
        )
        is ActionsTrigger.Other -> throw IllegalStateException("Unknown event ${trigger.name}")
    }
    val prefix = "gradle-build-cache-$jobId-$gradleVersion"
    return LayeredCache(
        name = "local-build-cache",
        baseline = "$prefix-$defaultBranch",
        primaryKey = "$prefix-$pkPrefix-$treeId",
        restoreKeys = restoreKeys.map { "$prefix-$it" },
        paths = listOf(
            "$buildCacheLocation/",
            "!$buildCacheLocation/gc.properties",
            "!$buildCacheLocation/build-cache-1.lock",
        ),
    )
}
