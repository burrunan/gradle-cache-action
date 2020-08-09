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
package com.github.burrunan.gradle

import actions.core.ActionFailedException
import actions.core.ActionStage
import actions.core.info
import actions.exec.exec
import com.github.burrunan.gradle.cache.*
import com.github.burrunan.gradle.github.suspendingStateVariable
import com.github.burrunan.launcher.GradleDistribution
import octokit.ActionsTrigger
import kotlin.js.Date
import kotlin.math.roundToInt

class GradleCacheAction(
    val trigger: ActionsTrigger,
    val params: Parameters,
    val gradleDistribution: GradleDistribution,
) {
    companion object {
        const val DEFAULT_BRANCH_VAR = "defaultbranch"
    }

    private val treeId = suspendingStateVariable("tree_id") {
        exec("git", "log", "-1", "--quiet", "--format=%T", captureOutput = true).stdout
    }

    suspend fun execute(stage: ActionStage) {
        val gradleVersion = gradleDistribution.version

        val caches = mutableListOf<Cache>()

        if (params.generatedGradleJars) {
            caches.add(gradleGeneratedJarsCache(gradleVersion))
        }

        if (params.localBuildCache) {
            caches.add(localBuildCache(params.jobId, trigger, gradleVersion, treeId.get()))
        }

        if (params.gradleDependenciesCache) {
            caches.add(gradleDependenciesCache(trigger, params.path, params.gradleDependenciesCacheKey))
        }

        if (params.mavenDependenciesCache) {
            caches.add(mavenDependenciesCache(trigger, params.path, params.mavenLocalIgnorePaths))
        }

        val cache = CompositeCache("all-caches", caches, concurrent = params.concurrent)
        when (stage) {
            ActionStage.MAIN -> {
                val started = Date.now()
                val restore = cache.restore()
                val elapsed = Date.now() - started
                info("Cache restore took ${(elapsed / 1000).roundToInt()} seconds")
            }
            ActionStage.POST -> cache.save()
            else -> throw ActionFailedException("Cache action should be called in PRE or POST stages only. " +
                "Current stage is $stage")
        }
    }
}
