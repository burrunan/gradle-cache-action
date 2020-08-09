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

package com.github.burrunan.launcher.internal

// e: /../build.gradle.kts:62:1: Unresolved reference: invalid
private val KOTLIN_COMPILE_ERROR = Regex("^e: (\\S.+?):(\\d+):(?:(\\d+):)? (.+)$")
// [ant:checkstyle] [ERROR] /.../SqlHopTableFunction.java:56:35: ',' is not followed by whitespace. [WhitespaceAfter]
private val CHECKSTYLE_ERROR = Regex("^\\[ant:checkstyle] \\[ERROR] (\\S.+?):(\\d+):(?:(\\d+):)? (.+) \\[([^\\]]+)]$")
// /.../RelDataType.java:249: error: reference not found
private val JAVA_ERROR = Regex("^(\\S.+?):(\\d+): error: (.+)$")

class GradleOutErrorCollector {
    private val _errors = mutableListOf<GradleError>()
    val errors: List<GradleError> = _errors
    private var taskName: String = "Unknown task"
    private var javaError: MatchResult? = null
    private val javaErrorLines = mutableListOf<String>()

    fun process(line: String) {
        if (line.startsWith("> Task ") ||
            line.startsWith("> Configure")
        ) {
            taskName = line.removePrefix("> ").let { "[$it]" }
        }
        if (line.startsWith("e: ")) {
            // Looks like Kotlin error
            // e: /../build.gradle.kts:62:1: Unresolved reference: invalid
            KOTLIN_COMPILE_ERROR.matchEntire(line)?.let {
                _errors += GradleError(
                    message = "$taskName ${it.groupValues[4]}",
                    file = it.groupValues[1],
                    line = it.groupValues[2].toInt(),
                    col = it.groupValues[3].takeIf { it.isNotBlank() }?.toInt(),
                )
            }
            return
        }
        // Checkstyle error:
        // [ant:checkstyle] [ERROR] /.../SqlHopTableFunction.java:56:35: ',' is not followed by whitespace. [WhitespaceAfter]
        CHECKSTYLE_ERROR.matchEntire(line)?.let {
            _errors += GradleError(
                message = "$taskName ${"[${it.groupValues[5]}] ".removePrefix("[] ")}${it.groupValues[4]}",
                file = it.groupValues[1],
                line = it.groupValues[2].toInt(),
                col = it.groupValues[3].takeIf { it.isNotBlank() }?.toInt(),
            )
        }
        processJavaError(line)
    }

    private fun processJavaError(line: String) {
        // /.../RelDataType.java:249: error: reference not found
        JAVA_ERROR.matchEntire(line)?.let {
            done()
            javaError = it
            return
        }
        if (javaError != null) {
            val errorContinuation = line.startsWith(" ")
            if (errorContinuation) {
                javaErrorLines += line
            }
            if (!errorContinuation || javaErrorLines.size >= 3) {
                done()
            }
        }
    }

    fun done() {
        javaError?.let {
            _errors += GradleError(
                message = "$taskName ${it.groupValues[3]}\n${javaErrorLines.joinToString("\n")}",
                file = it.groupValues[1],
                line = it.groupValues[2].toInt(),
            )
        }
        javaError = null
        javaErrorLines.clear()
    }
}
