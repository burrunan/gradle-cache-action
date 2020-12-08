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

import actions.core.*
import actions.core.ext.getInput
import actions.core.ext.getListInput
import actions.io.mkdirP
import com.github.burrunan.gradle.GradleCacheAction
import com.github.burrunan.gradle.Parameters
import com.github.burrunan.gradle.github.stateVariable
import com.github.burrunan.gradle.proxy.CacheProxy
import com.github.burrunan.launcher.LaunchParams
import com.github.burrunan.launcher.install
import com.github.burrunan.launcher.launchGradle
import com.github.burrunan.launcher.resolveDistribution
import fs2.promises.writeFile
import octokit.currentTrigger
import path.path

fun String.splitLines() =
    split(Regex("\\s*[\r\n]+\\s*"))
        .filter { !it.startsWith("#") && it.contains("=") }
        .associate {
            val values = it.split(Regex("\\s*=\\s*"), limit = 2)
            values[0] to (values.getOrNull(1) ?: "")
        }

fun isMochaRunning() =
    arrayOf("afterEach", "after", "beforeEach", "before", "describe", "it").all {
        global.asDynamic()[it] is Function<*>
    }

suspend fun main() {
    if (isMochaRunning()) {
        // Ignore if called from tests
        return
    }
    val stageVar = stateVariable("stage") { "MAIN" }
    val stage = ActionStage.values().firstOrNull { it.name == stageVar.get() }
    // Set next stage
    stageVar.set(
        when (stage) {
            ActionStage.MAIN -> ActionStage.POST
            null -> {
                setFailed("Unable to find action stage: ${stageVar.get()}")
                return
            }
            else -> null
        }?.name ?: "FINAL",
    )
    try {
        mainInternal(stage)
    } catch (e: ActionFailedException) {
        setFailed(e.message)
    }
}

suspend fun mainInternal(stage: ActionStage) {
    val gradleStartArguments = parseArgsStringToArgv(getInput("arguments")).toList()
    val cacheProxyEnabled = getInput("remote-build-cache-proxy-enabled").ifBlank { "true" }.toBoolean()

    val executionOnlyCaches = getInput("execution-only-caches").ifBlank { "false" }.toBoolean()

    val buildRootDirectory = getInput("build-root-directory").trimEnd('/', '\\')
    if (buildRootDirectory != "") {
        info("changing working directory to $buildRootDirectory")
        process.chdir(buildRootDirectory)
    }

    val params = Parameters(
        jobId = ActionsEnvironment.RUNNER_OS + "-" + getInput("job-id"),
        path = ".",
        debug = getInput("debug").toBoolean(),
        generatedGradleJars = getInput("save-generated-gradle-jars").ifBlank { "true" }.toBoolean(),
        localBuildCache = (!cacheProxyEnabled || gradleStartArguments.isEmpty()) && getInput("save-local-build-cache").ifBlank { "true" }
            .toBoolean(),
        gradleDependenciesCache = !executionOnlyCaches && getInput("save-gradle-dependencies-cache").ifBlank { "true" }.toBoolean(),
        gradleDependenciesCacheKey = getListInput("gradle-dependencies-cache-key"),
        mavenDependenciesCache = !executionOnlyCaches && getInput("save-maven-dependencies-cache").ifBlank { "true" }.toBoolean(),
        mavenLocalIgnorePaths = getListInput("maven-local-ignore-paths"),
        concurrent = getInput("concurrent").ifBlank { "false" }.toBoolean(),
    )

    val gradleDistribution = resolveDistribution(
        versionSpec = getInput("gradle-version").ifBlank { "wrapper" },
        projectPath = params.path,
        distributionUrl = getInput("gradle-distribution-url").ifBlank { null },
        distributionSha256Sum = getInput("gradle-distribution-sha-256-sum").ifBlank { null },
        enableDistributionSha256SumWarning = getInput("gradle-distribution-sha-256-sum-warning").ifBlank { "true" }.toBoolean(),
    )

    if (stage == ActionStage.MAIN || stage == ActionStage.POST) {
        val cacheAction = GradleCacheAction(currentTrigger(), params, gradleDistribution)

        if (params.generatedGradleJars || params.localBuildCache ||
            params.gradleDependenciesCache || params.mavenDependenciesCache
        ) {
            cacheAction.execute(stage)
        }
    }

    if (stage == ActionStage.MAIN && gradleStartArguments.isNotEmpty()) {
        val args = when (params.localBuildCache || cacheProxyEnabled) {
            true -> listOf("--build-cache") + gradleStartArguments
            else -> gradleStartArguments
        }
        val launchParams = LaunchParams(
            gradle = install(gradleDistribution),
            projectPath = params.path,
            arguments = args,
            properties = getInput("properties").splitLines(),
        )

        val cacheProxy = CacheProxy()

        if (cacheProxyEnabled) {
            info("Starting remote cache proxy, adding it via ~/.gradle/init.gradle")
            cacheProxy.start()
            val gradleHome = path.join(os.homedir(), ".gradle")
            mkdirP(gradleHome)
            writeFile(
                path.join(gradleHome, "init.gradle"),
                cacheProxy.getMultiCacheConfiguration(
                    multiCacheEnabled = getInput("multi-cache-enabled").ifBlank { "true" }.toBoolean(),
                    multiCacheVersion = getInput("multi-cache-version").ifBlank { "1.0" },
                    multiCacheRepository = getInput("multi-cache-repository"),
                    multiCacheGroupIdFilter = getInput("multi-cache-group-id-filter").ifBlank { "com[.]github[.]burrunan[.]multi-?cache" },
                ),
            )
        }

        try {
            val result = launchGradle(launchParams)
            result.buildScanUrl?.let {
                warning("Gradle Build Scan: $it")
                setOutput("build-scan-url", it)
            }
        } finally {
            if (cacheProxyEnabled) {
                cacheProxy.stop()
            }
        }
    }
    return
}
