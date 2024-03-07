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

package com.github.burrunan

import parseArgsStringToArgv
import kotlin.test.Test
import kotlin.test.assertEquals

class ArgumentsTest {
    private fun parse(input: String, vararg output: String) {
        assertEquals(listOf(*output), parseArgsStringToArgv(input).toList(), input)
    }

    @Test
    fun simple() {
        parse("")
        parse("a b", "a", "b")
        parse("a 'b'", "a", "b")
        parse("a \"b\"", "a", "b")
    }

    @Test
    fun multiline() {
        parse("a\nb", "a", "b")
        parse("a\n  b", "a", "b")
        parse("a\n  b  ", "a", "b")
        parse("a\n  b  \nc", "a", "b", "c")
    }

    @Test
    fun multilineWithQuotes() {
        parse("'a\nb'", "a\nb")
        parse("hello 'a\n  b' world", "hello", "a\n  b", "world")
        parse("hello \"a\n  b\" world", "hello", "a\n  b", "world")
    }

    @Test
    fun withDollars() {
        parse("\$HOME", "\$HOME")
    }

    @Test
    fun multilineWithComments() {
        // TODO: "# commented" should be ignored
        parse("""
            build
            # commented
            test
        """.trimIndent(), "build", "#", "commented", "test")
    }
}
