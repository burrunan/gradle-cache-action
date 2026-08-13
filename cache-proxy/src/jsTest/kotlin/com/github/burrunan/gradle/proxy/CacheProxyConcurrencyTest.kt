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

import com.github.burrunan.gradle.cache.CacheService
import com.github.burrunan.test.runTest
import js.objects.unsafeJso
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import node.buffer.BufferEncoding
import node.buffer.utf8
import node.fs.readFile
import node.http.IncomingMessage
import node.http.RequestOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * Fails when requests that share the temp file of one build cache entry id run over each other.
 *
 * Both handlers name that file after the id alone, so a store that archives a file another request has
 * replaced uploads a truncated payload under a key later builds treat as valid, and a read of a file
 * being written returns one. The cases below hold a store at the cache service and drive a second
 * request for the same id into it, which is the overlap #176 reports and what a build log never shows.
 */
class CacheProxyConcurrencyTest {
    // Emulates Azure Cache Backend for @actions/cache
    private val cacheService = CacheService()

    // Implements Gradle HTTP Build Cache via @actions/cache
    private val cacheProxy = CacheProxy()

    private val firstPayload = "first payload ".repeat(4096)
    private val secondPayload = "second payload ".repeat(4096)

    private class ProxyResponse(val statusCode: Int, val body: String)

    /**
     * Sends one request to the proxy and reads the whole response.
     *
     * @param payload request body, or null for a request that carries none
     */
    private suspend fun call(method: String, id: String, payload: String? = null): ProxyResponse {
        val url = assertNotNull(cacheProxy.cacheUrl, "the proxy must publish its URL once started") + id
        val response = suspendCoroutine { continuation ->
            val request = node.http.request(
                url,
                unsafeJso<RequestOptions> { this.method = method },
            ) { response: IncomingMessage ->
                continuation.resume(response)
            }
            payload?.let { request.write(it) }
            request.end()
        }
        return ProxyResponse(response.statusCode?.toInt() ?: 0, node.stream.consumers.text(response))
    }

    private suspend fun put(id: String, payload: String) = call("PUT", id, payload)

    private suspend fun get(id: String) = call("GET", id)

    /**
     * Sends the head of a body and then drops the connection, the way a build killed mid-upload does.
     *
     * Returns once the proxy has seen the connection go, so what follows can tell a released id from a
     * stranded one rather than racing the release.
     */
    private suspend fun putAndAbort(id: String, payload: String) {
        val url = assertNotNull(cacheProxy.cacheUrl, "the proxy must publish its URL once started") + id
        val closed = CompletableDeferred<Unit>()
        val request = node.http.request(
            url,
            unsafeJso<RequestOptions> { method = "PUT" },
        ) { }
        // Destroying a request in flight raises 'socket hang up' on it, and an unobserved 'error' on a
        // client request terminates the process
        request.errorEvent.addHandler { }
        request.closeEvent.addHandler { closed.complete(Unit) }
        request.write(payload.take(payload.length / 2))
        try {
            // Destroying the connection before the proxy has taken the id would abort nothing at all,
            // since the head of the body is written to a socket rather than handed to the server
            assertTrue(
                awaitEntry(id, held = true),
                "the proxy must have taken $id before the connection to it goes",
            )
        } finally {
            // A request left in flight parks putEntry in pipeline() waiting for a body end that never
            // comes, and nothing closes that connection for the five minutes Node's requestTimeout
            // allows. Mocha runs without --exit, so a failure here would report and then hang the suite.
            request.destroy()
        }
        closed.await()
    }

    /**
     * Waits until [id] is held by a request, or until no request holds it any more, and answers whether
     * it got there.
     *
     * A wait that runs out returns `false` rather than throwing, so a caller keeps its own assertion and
     * the message that goes with it. What the wait removes is the deadline: an id that reaches the state
     * in 10 ms and an id that reaches it in 900 ms both pass, and only an id that never reaches it at
     * all fails. Giving up well inside the 60 s the test framework allows is what keeps that failure an
     * assertion rather than a timeout.
     */
    private suspend fun awaitEntry(id: String, held: Boolean): Boolean =
        withTimeoutOrNull(10.seconds) {
            while (CacheProxy.holdsEntry(id) != held) {
                delay(10)
            }
        } != null

    /**
     * Runs [block] with every save of [id] held at the cache service, and lets those saves go afterwards.
     *
     * A save reserves its key once it has archived the temp file, so a store held here has left the
     * proxy and is waiting on the network, which is the state a second request has to survive. The
     * release has to happen even when [block] fails, since [CacheService.stop] waits for the requests
     * still open and would swallow the failure in a timeout of the whole test.
     */
    private suspend fun withSavesHeld(id: String, block: suspend (SaveGate) -> Unit) {
        val gate = SaveGate()
        cacheService.beforeReserveCache = { key ->
            if (key.endsWith(id)) {
                gate.reached.complete(Unit)
                gate.released.await()
            }
        }
        try {
            block(gate)
        } finally {
            gate.release()
        }
    }

    private class SaveGate {
        val reached = CompletableDeferred<Unit>()
        val released = CompletableDeferred<Unit>()

        /** Waits until a save of the held id reaches the cache service. */
        suspend fun awaitSave() = withTimeout(30.seconds) {
            reached.await()
        }

        fun release() = released.complete(Unit)
    }

    /** Lets a request launched moments ago reach the point where it waits for the id. */
    private suspend fun letTheOthersRun() = delay(100)

    @Test
    fun aDuplicateStoreLeavesTheEntryOfTheRunningStoreAlone() = runTest {
        val id = "duplicate-store"
        cacheService {
            cacheProxy {
                withSavesHeld(id) { gate ->
                    assertEquals(200, put(id, firstPayload).statusCode, "PUT of $id")
                    gate.awaitSave()
                    assertEquals(200, put(id, secondPayload).statusCode, "second PUT of $id")
                    assertEquals(
                        firstPayload,
                        readFile(".cache-proxy/bc-$id", BufferEncoding.utf8),
                        "the temp file the running store is uploading",
                    )
                    gate.release()
                    assertEquals(firstPayload, get(id).body, "GET of $id once its store has finished")
                }
            }
        }
    }

    @Test
    fun aGetWaitsForTheStoreThatHoldsTheEntry() = runTest {
        val id = "get-during-store"
        cacheService {
            cacheProxy {
                withSavesHeld(id) { gate ->
                    assertEquals(200, put(id, firstPayload).statusCode, "PUT of $id")
                    gate.awaitSave()
                    coroutineScope {
                        val restored = async { get(id) }
                        letTheOthersRun()
                        assertFalse(
                            restored.isCompleted,
                            "a GET must not restore into the temp file the running store is uploading",
                        )
                        gate.release()
                        assertEquals(firstPayload, restored.await().body, "GET of $id")
                    }
                }
            }
        }
    }

    @Test
    fun aPutThatDropsItsConnectionStoresNothingAndHandsTheIdBack() = runTest {
        val id = "aborted-put"
        cacheService {
            cacheProxy {
                // Saves are held, so an id still held once the dust settles means the half a payload
                // that did arrive went on to be archived
                withSavesHeld(id) {
                    putAndAbort(id, firstPayload)
                    awaitEntry(id, held = false)
                    assertFalse(
                        CacheProxy.holdsEntry(id),
                        "a PUT that lost its connection must archive nothing and leave $id to others",
                    )
                }
                assertEquals(200, put(id, secondPayload).statusCode, "PUT of $id after an aborted one")
                assertEquals(secondPayload, get(id).body, "GET of $id after an aborted PUT")
            }
        }
    }

    @Test
    fun concurrentGetsOfOneIdEachReturnTheWholeEntry() = runTest {
        val id = "concurrent-gets"
        cacheService {
            cacheProxy {
                assertEquals(200, put(id, firstPayload).statusCode, "PUT of $id")
                // The first GET waits out the store, so the entry is in the cache service from here on
                assertEquals(firstPayload, get(id).body, "GET of $id")
                coroutineScope {
                    val bodies = (1..4).map { async { get(id).body } }.awaitAll()
                    assertEquals(List(4) { firstPayload }, bodies, "four concurrent GETs of $id")
                }
            }
        }
    }
}
