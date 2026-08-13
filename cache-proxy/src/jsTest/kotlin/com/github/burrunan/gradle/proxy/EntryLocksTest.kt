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

package com.github.burrunan.gradle.proxy

import com.github.burrunan.test.runTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Fails when two requests for one build cache entry id can hold its temp file at the same time.
 *
 * The proxy names that file after the id alone, so an overlap means one request reads or writes what
 * another one is writing: a store that archives a file replaced underneath it publishes a truncated
 * entry, and every later build restores that entry as a valid one. [EntryLocks] is what keeps the
 * requests apart, and it also has to let the id go afterwards, since a build stores thousands of them.
 */
class EntryLocksTest {
    private val locks = EntryLocks()

    /** Lets a coroutine launched moments ago reach its first suspension point. */
    private suspend fun letTheOthersRun() = delay(50)

    @Test
    fun oneIdRunsOneRequestAtATime() = runTest {
        val order = mutableListOf<String>()
        val firstIsIn = CompletableDeferred<Unit>()
        val firstMayLeave = CompletableDeferred<Unit>()
        coroutineScope {
            launch {
                locks.withEntry("id") {
                    order += "first in"
                    firstIsIn.complete(Unit)
                    firstMayLeave.await()
                    order += "first out"
                }
            }
            firstIsIn.await()
            launch {
                locks.withEntry("id") {
                    order += "second in"
                }
            }
            letTheOthersRun()
            assertEquals(listOf("first in"), order, "the second request must wait for the first one")
            firstMayLeave.complete(Unit)
        }
        assertEquals(listOf("first in", "first out", "second in"), order)
        assertEquals(0, locks.trackedIds, "ids held after both requests are done")
    }

    @Test
    fun differentIdsDoNotWaitForEachOther() = runTest {
        locks.withEntry("one") {
            locks.withEntry("two") {
                // Reaching this at all is the assertion: a shared lock would deadlock the test instead
            }
        }
        assertEquals(0, locks.trackedIds, "ids held after both requests are done")
    }

    @Test
    fun aStoreIsAdmittedOnlyOnceUntilItEnds() = runTest {
        assertTrue(locks.beginStore("id"), "the first store of an idle id")
        assertFalse(locks.beginStore("id"), "a second store while the first one is under way")
        locks.endStore("id")
        assertTrue(locks.beginStore("id"), "a store of an id whose previous store has ended")
        locks.endStore("id")
        assertEquals(0, locks.trackedIds, "ids held after both stores are done")
    }

    @Test
    fun aRequestWaitsForTheStoreThatHoldsTheId() = runTest {
        var restored = false
        assertTrue(locks.beginStore("id"))
        coroutineScope {
            launch {
                locks.withEntry("id") {
                    restored = true
                }
            }
            letTheOthersRun()
            assertFalse(restored, "a request must not read the temp file a store is uploading")
            locks.endStore("id")
        }
        assertTrue(restored, "a request must get the id once the store has ended")
        assertEquals(0, locks.trackedIds, "ids held after the store and the request are done")
    }
}
