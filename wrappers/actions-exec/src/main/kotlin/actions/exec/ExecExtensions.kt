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

import kotlinext.js.jsObject
import kotlinx.coroutines.await

class ExecResult(
    val exitCode: Int,
    val stdout: String,
)

suspend fun exec(
    commandLine: String, vararg args: String,
    captureOutput: Boolean = false,
    options: ExecOptions.() -> Unit = {}
): ExecResult {
    val stdout = mutableListOf<String>()
    val exitCode = exec(
        commandLine,
        args.copyOf(),
        jsObject {
            // TODO: add custom interface for ExecOptions for [captureOutput]
            listeners = jsObject()
            options()
            if (captureOutput) {
                listeners!!.stdout = {
                    // it.toString() results in [...] for unknown reason
                    stdout.add("" + it.unsafeCast<String>())
                }
            }
        }
    ).await()
    return ExecResult(
        exitCode = exitCode.toInt(),
        stdout = stdout.joinToString("\n")
    )
}
