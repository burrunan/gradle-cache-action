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
package com.github.burrunan.gradle.cache

import Buffer

class CacheStorage {
    private val storage = mutableMapOf<String, CacheEntry>()
    private val reservations = mutableMapOf<String, CacheReservation>()
    private val caches = mutableMapOf<Number, TemporaryCache>()
    private var nextId = 0

    fun reserveCache(key: String, version: String): Number? {
        if (key in storage || key in reservations) {
            return null
        }
        if (reservations[key]?.version?.equals(version) == false) {
            return null
        }
        nextId += 1
        reservations[key] = CacheReservation(nextId, version)
        caches[nextId] = TemporaryCache(key)
        return nextId
    }

    operator fun set(key: String, value: CacheEntry) {
        storage[key] = value
    }

    operator fun get(key: String) = storage[key]

    fun getValue(key: String) = storage.getValue(key)

    fun find(prefix: String, version: String) =
        storage.filterKeys { it.startsWith(prefix) }
            .filterValues { it.version == version }
            .maxByOrNull { it.value.creationTime.toDouble() }

    fun update(cacheId: Number, start: Int, end: Int, buffer: Buffer) {
        caches.getValue(cacheId).parts.add(UploadPart(start, end, buffer))
    }

    fun commitCache(cacheId: Number, size: Number) {
        val cache = caches.remove(cacheId)
            ?: throw HttpException.noContent("Cache $cacheId is not found")
        val reservation = reservations.remove(cache.key)
            ?: throw HttpException.noContent("Reservation ${cache.key} is not found for cache $cacheId")

        val parts = cache.parts
        val result = if (parts.size == 1 && parts[0].contents.length == size) {
            parts[0].contents
        } else {
            Buffer.alloc(size).also {
                for (part in parts) {
                    part.contents.copy(it, part.start, 0, part.end)
                }
            }
        }
        set(cache.key, CacheEntry(reservation.version, result, cacheId))
    }
}

class CacheEntry(val version: String, val value: Buffer, val creationTime: Number)

class CacheReservation(val number: Number, val version: String)

class TemporaryCache(val key: String) {
    val parts = mutableListOf<UploadPart>()
}

class UploadPart(val start: Int, val end: Int, val contents: Buffer)
