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

import actions.core.ActionFailedException
import parseCacheProxyPort
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

/**
 * Fails when `remote-build-cache-proxy-port` accepts a value the proxy cannot bind, or rejects one it can.
 *
 * Every rejection must reach the user as an [ActionFailedException], since that is the only type
 * `main` turns into `setFailed`. Anything else leaves the step with a raw Node stack trace that
 * never mentions the input.
 */
class CacheProxyPortTest {
    @Test
    fun acceptsIntegersTheProxyCanBind() {
        val accepted = mapOf(
            "" to 0,
            "   " to 0,
            "0" to 0,
            "1" to 1,
            " 34567 " to 34567,
            "65534" to 65534,
            "65535" to 65535,
        )
        for ((input, expected) in accepted) {
            assertEquals(expected, parseCacheProxyPort(input), "parseCacheProxyPort('$input')")
        }
    }

    @Test
    fun rejectsValuesOutsideTheRangeAndNonIntegers() {
        val rejected = listOf("-1", "65536", "70000", "abc", "1.5", "0x10", "12 34", "+")
        for (input in rejected) {
            val failure = assertFailsWith<ActionFailedException>("parseCacheProxyPort('$input')") {
                parseCacheProxyPort(input)
            }
            val message = assertNotNull(failure.message, "parseCacheProxyPort('$input')")
            assertContains(message, "remote-build-cache-proxy-port", message = message)
            assertContains(message, input, message = message)
        }
    }
}
