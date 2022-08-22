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

@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package actions.exec

import node.buffer.Buffer
import node.process.ProcessEnv

import node.stream.Writable

external interface ExecListeners {
    var stdout: ((data: Buffer) -> Unit)?
    var stderr: ((data: Buffer) -> Unit)?
    var stdline: ((data: String) -> Unit)?
    var errline: ((data: String) -> Unit)?
    var debug: ((data: String) -> Unit)?
}

external interface ExecOptions {
    var cwd: String?
    var env: ProcessEnv?
    var silent: Boolean?
    var outStream: Writable?
    var errStream: Writable?
    var windowsVerbatimArguments: Boolean?
    var failOnStdErr: Boolean?
    var ignoreReturnCode: Boolean?
    var delay: Number?
    var input: Buffer?
    var listeners: ExecListeners?
}
