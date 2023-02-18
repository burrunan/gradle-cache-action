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

package actions.core

import js.core.jso

external interface TypedCommandProperties : CommandProperties {
    var file: String
    var line: Int
    var col: Int
}

inline fun issueCommand(command: String, message: String, properties: TypedCommandProperties.() -> Unit = {}) =
    issueCommand(command, jso(properties), message)

fun warning(message: String, file: String? = null, line: Int? = null, col: Int? = null) {
    if (file == null) {
        issueCommand("warning", message)
        return
    }
    issueCommand("warning", message) {
        this.file = file
        line?.let { v -> this.line = v }
        col?.let { v -> this.col = v }
    }
}

fun error(message: String, file: String? = null, line: Int? = null, col: Int? = null) {
    if (file == null) {
        issueCommand("error", message)
        return
    }
    issueCommand("error", message) {
        this.file = file
        line?.let { v -> this.line = v }
        col?.let { v -> this.col = v }
    }
}
