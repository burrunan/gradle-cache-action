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
package actions.exec

class ExecResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String,
)

suspend fun exec(
    commandLine: String, vararg args: String,
    captureOutput: Boolean = false,
    options: (ExecOptions) -> ExecOptions = { it }
): ExecResult {
    val stdout = mutableListOf<String>()
    val stderr = mutableListOf<String>()
    val execOptions = if (!captureOutput) {
        ExecOptions()
    } else {
        ExecOptions(
            listeners = ExecListeners(
                stdout = {
                    // it.toString() results in [...] for unknown reason
                    stdout.add("" + it.unsafeCast<String>())
                },
                stderr = {
                    // it.toString() results in [...] for unknown reason
                    stderr.add("" + it.unsafeCast<String>())
                }
            )
        )
    }
    val exitCode = exec(
        commandLine,
        args.copyOf(),
        options(execOptions),
    )
    return ExecResult(
        exitCode = exitCode.toInt(),
        stdout = stdout.joinToString("\n"),
        stderr = stderr.joinToString("\n")
    )
}
