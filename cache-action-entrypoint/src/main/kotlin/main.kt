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

import actions.core.ext.getInput
import actions.core.info
import com.github.burrunan.gradle.GradleCacheAction
import com.github.burrunan.gradle.Parameters
import com.github.burrunan.gradle.github.env.ActionsEnvironment
import com.github.burrunan.gradle.github.event.currentTrigger

suspend fun main() {
    if (process.env["GITHUB_ACTIONS"].isNullOrBlank()) {
        // Ignore if called outside of GitHub Actions (e.g. tests)
        return
    }
    val params = Parameters(
        jobId = ActionsEnvironment.RUNNER_OS + "-" + getInput("job-id"),
        path = getInput("path").trimEnd('/', '\\').ifBlank { "." },
        debug = getInput("debug").toBoolean(),
        generatedGradleJars = getInput("save-generated-gradle-jars").ifBlank { "true" }.toBoolean(),
        localBuildCache = getInput("save-local-build-cache").ifBlank { "true" }.toBoolean(),
        gradleDependenciesCache = getInput("save-gradle-dependencies-cache").ifBlank { "true" }.toBoolean(),
        gradleDependenciesCacheKey = getInput("gradle-dependencies-cache-key"),
        mavenDependenciesCache = getInput("save-maven-dependencies-cache").ifBlank { "true" }.toBoolean(),
        concurrent = getInput("concurrent").ifBlank { "false" }.toBoolean(),
    )

    if (!params.generatedGradleJars && !params.localBuildCache &&
        !params.gradleDependenciesCache && !params.mavenDependenciesCache
    ) {
        info("All the caches are disabled, skipping the action")
        return
    }

    GradleCacheAction(currentTrigger(), params).run()
}
