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

import kotlinx.coroutines.sync.Mutex

/**
 * Serialises the requests that share the temp file of one build cache entry id.
 *
 * Every payload for an id passes through a temp file named after that id, and giving each request a name
 * of its own does not work: `@actions/cache` extracts an archive under the path recorded when the archive
 * was created, so an entry stored as `bc-42-2` would come back as `bc-42-2` while the reader waits on
 * `bc-42`. Requests for one id run one at a time instead. Different ids never wait on each other.
 */
internal class EntryLocks {
    private class Entry {
        val mutex = Mutex()

        /** Requests holding or waiting for [mutex]. The entry leaves the map once none are left. */
        var users = 0

        /** Whether a store has taken this id and has yet to upload the archive and clean up after it. */
        var storing = false
    }

    private val entries = mutableMapOf<String, Entry>()

    /**
     * Number of ids that hold state here, which is zero while no request is in flight.
     *
     * A build stores thousands of entries, so an id left behind by its last request would cost a lock
     * per entry for as long as the action runs.
     */
    internal val trackedIds: Int get() = entries.size

    /** Whether any request holds or waits for the temp file of [id]. */
    internal fun holds(id: String): Boolean = id in entries

    /** Runs [block] with the temp file of [id] to itself, waiting out any request that holds it. */
    suspend fun <T> withEntry(id: String, block: suspend () -> T): T {
        val entry = lock(id, entries.getOrPut(id) { Entry() })
        try {
            return block()
        } finally {
            release(id, entry)
        }
    }

    /**
     * Takes the temp file of [id] for a store, or answers `false` when a store for that id is under way.
     *
     * A store outlives the request that starts it: the PUT is answered once the body reaches the temp
     * file, and the archive uploads afterwards. The lock it takes is released by [endStore] rather than
     * by returning from here.
     */
    suspend fun beginStore(id: String): Boolean {
        val entry = entries.getOrPut(id) { Entry() }
        if (entry.storing) {
            return false
        }
        entry.storing = true
        try {
            lock(id, entry)
        } catch (e: Throwable) {
            entry.storing = false
            throw e
        }
        return true
    }

    /** Releases what [beginStore] took, so the next request for [id] can have the temp file. */
    fun endStore(id: String) {
        val entry = entries.getValue(id)
        entry.storing = false
        release(id, entry)
    }

    private suspend fun lock(id: String, entry: Entry): Entry {
        entry.users++
        try {
            entry.mutex.lock()
        } catch (e: Throwable) {
            // A wait that ends in a cancellation never reaches release()
            dropUser(id, entry)
            throw e
        }
        return entry
    }

    private fun release(id: String, entry: Entry) {
        dropUser(id, entry)
        entry.mutex.unlock()
    }

    private fun dropUser(id: String, entry: Entry) {
        entry.users--
        if (entry.users == 0) {
            entries.remove(id)
        }
    }
}
