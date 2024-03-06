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
package com.github.burrunan.launcher

import actions.core.ExitCode
import actions.core.setFailed
import actions.core.setOutput
import actions.exec.exec
import com.github.burrunan.launcher.internal.GradleErrorCollector
import com.github.burrunan.launcher.internal.GradleOutErrorCollector
import js.objects.jso
import node.process.process

class GradleResult(
    val buildScanUrl: String?,
)

suspend fun launchGradle(params: LaunchParams): GradleResult {
    var buildScanUrl: String? = null
    // See https://youtrack.jetbrains.com/issue/KT-41107
    var failureDetected = false
    val errorCollector = GradleErrorCollector()
    val outCollector = GradleOutErrorCollector()

    @Suppress("REDUNDANT_SPREAD_OPERATOR_IN_NAMED_FORM_IN_FUNCTION")
    val result = exec(
        params.gradle,
        args = *(listOf("--no-daemon") +
            params.properties.map { "-P${it.key}=${it.value}" } +
            params.arguments).toTypedArray(),
    ) {
        cwd = params.projectPath
        ignoreReturnCode = true
        listeners = jso {
            stdline = {
                val str = it.trimEnd()
                if (str.startsWith("https://gradle.com/s/")) {
                    setOutput("build-scan-url", str)
                    buildScanUrl = str
                }
                outCollector.process(str)
            }
            errline = {
                errorCollector.process(it)
                outCollector.process(it)
            }
        }
    }
    errorCollector.done()
    outCollector.done()
    for (error in errorCollector.errors + outCollector.errors) {
        failureDetected = true
        val shortFile = error.file
            ?.removePrefix(process.cwd())
        actions.core.error(
            error.message,
            jso {
                file = shortFile
                startLine = error.line
                startColumn = error.col
            },
        )
    }
    if (failureDetected) {
        process.exitCode = ExitCode.Failure.unsafeCast<Double>()
    }
    if (!failureDetected && result.exitCode != 0) {
        setFailed("Gradle process finished with a non-zero exit code: ${result.exitCode}")
    }
    return GradleResult(buildScanUrl)
}
