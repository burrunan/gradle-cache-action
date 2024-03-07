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

class GradleOutCollectorTest {
    @Test
    fun koltlinCompileErrors() {
        testCollector(
            """
                GradleError(line=62, col=1, file=/home/runner/work/pgjdbc/pgjdbc/build.gradle.kts, message='[Configure project :] Unresolved reference: invalid')
                GradleError(line=62, col=14, file=/home/runner/work/pgjdbc/pgjdbc/build.gradle.kts, message='[Configure project :] Unresolved reference: here')
            """.trimIndent(),
            """
                > Configure project :
                Evaluating root project 'pgjdbc' using build file '/home/runner/work/pgjdbc/pgjdbc/build.gradle.kts'.
                Loading cache entry 'cache/eila5i6e0q7sxpvg89w345ymz' from S3 bucket
                e: /home/runner/work/pgjdbc/pgjdbc/build.gradle.kts:62:1: Unresolved reference: invalid
                e: /home/runner/work/pgjdbc/pgjdbc/build.gradle.kts:62:14: Unresolved reference: here
            """.trimIndent(),
        )
    }

    @Test
    fun checkstyleError() {
        testCollector(
            """
                GradleError(line=56, col=35, file=/Users/runner/work/calcite/calcite/core/src/main/java/org/apache/calcite/sql/SqlHopTableFunction.java, message='[Task :core:checkstyleMain] [WhitespaceAfter] ',' is not followed by whitespace.')
                GradleError(line=32, col=null, file=/code/calcite/linq4j/src/main/java/org/apache/calcite/linq4j/AbstractEnumerable.java, message='[Task :core:checkstyleMain] [Indentation] 'method def modifier' has incorrect indentation level 0, expected level should be 2.')
            """.trimIndent(),
            """
                > Task :core:checkstyleMain
                [ant:checkstyle] [ERROR] /Users/runner/work/calcite/calcite/core/src/main/java/org/apache/calcite/sql/SqlHopTableFunction.java:56:35: ',' is not followed by whitespace. [WhitespaceAfter]
                [ant:checkstyle] [ERROR] /code/calcite/linq4j/src/main/java/org/apache/calcite/linq4j/AbstractEnumerable.java:32: 'method def modifier' has incorrect indentation level 0, expected level should be 2. [Indentation]
            """.trimIndent(),
        )
    }

    @Test
    fun javadocError() {
        testCollector(
            """
                GradleError(line=249, col=null, file=/Users/../type/RelDataType.java, message='[Task :babel:javadoc] reference not found
                   * {@link #equals(Object)}.
                            ^')
            """.trimIndent(),
            """
                > Task :babel:javadoc
                /Users/runner/runners/2.263.0/work/calcite/calcite/core/src/main/java/org/apache/calcite/rel/metadata/RelMetadataQuery.java:632: warning: no @param for rel
                  public List<Double> getAverageColumnSizesNotNull(RelNode rel) {
                                      ^
                /Users/runner/runners/2.263.0/work/calcite/calcite/core/src/main/java/org/apache/calcite/rel/metadata/RelMetadataQuery.java:632: warning: no @return
                  public List<Double> getAverageColumnSizesNotNull(RelNode rel) {
                                      ^
                /Users/../type/RelDataType.java:249: error: reference not found
                   * {@link #equals(Object)}.
                            ^
            """.trimIndent(),
        )
    }

    @Test
    fun javacError() {
        testCollector(
            """
                GradleError(line=46, col=null, file=/home/runner/../ReaderInputStreamTest.java, message='[Task :compileJava] cannot find symbol
                    Arrays.fill(acutal, (byte) 0x00);
                                ^
                  symbol:   variable acutal')
            """.trimIndent(),
            """
                > Task :compileJava
                Compiling with JDK Java compiler API.
                /home/runner/../ReaderInputStreamTest.java:46: error: cannot find symbol
                    Arrays.fill(acutal, (byte) 0x00);
                                ^
                  symbol:   variable acutal
                  location: class ReaderInputStreamTest
                Note: Some input files use or override a deprecated API.
                Note: Recompile with -Xlint:deprecation for details.
            """.trimIndent(),
        )
    }

    private fun testCollector(expected: String, input: String) {
        val collector = GradleOutErrorCollector()
        input.lines().forEach { collector.process(it) }

        collector.done()

        assertEquals(expected, collector.errors.joinToString("\n"))
    }
}
