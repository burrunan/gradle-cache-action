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

import kotlin.test.Test
import kotlin.test.assertEquals

class GradleErrorCollectorTest {
    @Test
    fun buildScriptFailure() {
        testCollector(
            """
                GradleError(line=62, col=null, file=/home/runner/work/pgjdbc/pgjdbc/build.gradle.kts, message='Script compilation errors:

                  Line 62: invalid code here
                           ^ Unresolved reference: invalid

                  Line 62: invalid code here
                                        ^ Unresolved reference: here

                2 errors')
            """.trimIndent(),
            """
                * Where:
                Build file '/home/runner/work/pgjdbc/pgjdbc/build.gradle.kts' line: 62

                * What went wrong:
                Script compilation errors:

                  Line 62: invalid code here
                           ^ Unresolved reference: invalid

                  Line 62: invalid code here
                                        ^ Unresolved reference: here

                2 errors

                * Try:
                Run with --stacktrace option to get the stack trace. Run with --debug option to get more log output. Run with --scan to get full insights.

                * Get more help at https://help.gradle.org

                BUILD FAILED in 44s
            """.trimIndent(),
        )
    }

    @Test
    fun noLocation() {
        testCollector(
            """
                GradleError(line=null, col=null, file=null, message='Task 'asdfasdf' not found in root project 'pgjdbc'.')
            """.trimIndent(),
            """
                See https://docs.gradle.org/6.3/userguide/command_line_interface.html#sec:command_line_warnings

                FAILURE: Build failed with an exception.

                * What went wrong:
                Task 'asdfasdf' not found in root project 'pgjdbc'.

                * Try:
                Run gradle tasks to get a list of available tasks. Run with --stacktrace option to get the stack trace. Run with --debug option to get more log output. Run with --scan to get full insights.

                * Get more help at https://help.gradle.org

                BUILD FAILED in 37s
            """.trimIndent(),
        )
    }

    private fun testCollector(expected: String, input: String) {
        val collector = GradleErrorCollector()
        input.lines().forEach { collector.process(it) }

        collector.done()

        assertEquals(expected, collector.errors.joinToString("\n"))
    }
}
