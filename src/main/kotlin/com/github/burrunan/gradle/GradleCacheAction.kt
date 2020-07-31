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
package com.github.burrunan.gradle

import com.github.burrunan.gradle.cache.*
import com.github.burrunan.gradle.github.event.ActionsTrigger
import com.github.burrunan.gradle.github.stateVariable
import com.github.burrunan.gradle.github.suspendingStateVariable
import com.github.burrunan.gradle.github.toBoolean
import fs2.promises.readFile
import github.actions.exec.exec
import kotlinx.coroutines.await

class GradleCacheAction(val trigger: ActionsTrigger, val params: Parameters) {
    private val treeId = suspendingStateVariable("tree_id") {
        exec("git", "log", "-1", "--quiet", "--format=%T").stdout
    }

    suspend fun run() {
        val gradleVersion = suspendingStateVariable("gradleVersion") {
            determineGradleVersion(params.path)
        }

        val caches = mutableListOf<Cache>()

        if (params.generatedGradleJars) {
            caches.add(gradleGeneratedJarsCache(gradleVersion.get()))
        }

        if (params.localBuildCache) {
            caches.add(localBuildCache(params.jobId, trigger, gradleVersion.get(), treeId.get()))
        }

        if (params.gradleDependenciesCache) {
            caches.add(gradleDependenciesCache(trigger, params.path))
        }

        if (params.mavenDependenciesCache) {
            caches.add(mavenDependenciesCache(trigger, params.path))
        }

        val cache = CompositeCache("all-caches", caches, concurrent = params.concurrent)
        val post = stateVariable("POST").toBoolean()
        if (post.get()) {
            cache.save()
        } else {
            post.set(true)
            cache.restore()
        }
    }

    private suspend fun determineGradleVersion(path: String): String {
        val gradleWrapperProperties = "$path/gradle/wrapper/gradle-wrapper.properties"
        val gradleVersion = getInput("gradle-version").ifBlank { "wrapper" }
        if (!gradleVersion.equals("wrapper", ignoreCase = true)) {
            return gradleVersion
        }
        val props = readFile(gradleWrapperProperties, "utf8").await()
        val distributionUrlRegex = Regex("\\s*distributionUrl\\s*=\\s*([^#]+)")
        val distributionUrl = props.split(Regex("[\r\n]+"))
            .filter { !it.startsWith("#") && it.contains("distributionUrl") }
            .mapNotNull { distributionUrlRegex.matchEntire(it)?.groupValues?.get(1) }
            .firstOrNull()
        return if (distributionUrl
                ?.removePrefix("https")?.removePrefix("http")
                ?.startsWith("\\://services.gradle.org/") == true
        ) {
            // Official release, use shorter version
            //   https://services.gradle.org/distributions-snapshots/gradle-6.7-20200730220045+0000-all.zip
            //   https://services.gradle.org/distributions/gradle-6.6-rc-4-all.zip
            //   https://services.gradle.org/distributions/gradle-6.5.1-all.zip
            distributionUrl.substringAfterLast("/").substringBefore(".zip")
        } else {
            hashFiles(gradleWrapperProperties, algorithm = "sha1").hash
        }
    }
}
