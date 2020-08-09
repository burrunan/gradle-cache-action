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

private enum class ErrorHeader(val message: String) {
    FAILURE("FAILURE: "),
    WHERE("* Where:"),
    WHAT_WENT_WRONG("* What went wrong:"),
    TRY("* Try:"),
}

private val errorHeaderValues = ErrorHeader.values()

class GradleErrorCollector {
    private val _errors = mutableListOf<GradleError>()
    val errors: List<GradleError> = _errors

    private val sb = StringBuilder()
    private var nextKey: ErrorHeader? = null
    private val data = mutableMapOf<ErrorHeader, String>()

    fun done() {
        if (data.isNotEmpty()) {
            val message = data[ErrorHeader.WHAT_WENT_WRONG] ?: "Unknown error"

            _errors += data[ErrorHeader.WHERE]?.let { location ->
                Regex("^Build file '(.+)' line: (\\d+)$").matchEntire(location)?.let {
                    GradleError(
                        message = message,
                        file = it.groupValues[1],
                        line = it.groupValues[2].toInt(),
                    )
                }
            } ?: GradleError(message)
        }
        data.clear()
        sb.clear()
    }

    fun process(line: String) {
        val str = line.trimEnd()
        if (str.startsWith(ErrorHeader.FAILURE.message)) {
            done()
            data[ErrorHeader.FAILURE] = str.removePrefix(ErrorHeader.FAILURE.message)
            return
        }

        if (str.startsWith("* Get more help") ||
            str.startsWith("BUILD FAILED ")
        ) {
            done()
            nextKey = null
            return
        }

        errorHeaderValues.firstOrNull { str.startsWith(it.message) }?.let {
            nextKey?.let {
                data[it] = sb.toString().trimEnd()
            }
            sb.clear()
            nextKey = it
            return
        }

        if (nextKey != null) {
            sb.appendLine(line)
        }
    }
}
