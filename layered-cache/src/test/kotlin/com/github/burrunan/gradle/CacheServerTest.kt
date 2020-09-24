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
 *
 */
package com.github.burrunan.gradle

import actions.cache.RestoreType
import actions.cache.restoreAndLog
import actions.cache.saveAndLog
import com.github.burrunan.gradle.cache.CacheService
import com.github.burrunan.gradle.cache.LayeredCache
import com.github.burrunan.test.runTest
import com.github.burrunan.wrappers.nodejs.mkdir
import fs2.promises.readFile
import fs2.promises.writeFile
import kotlin.test.Test
import kotlin.test.assertEquals

class CacheServerTest {
    val cacheService = CacheService()

    @Test
    fun saveCache() = runTest {
        val dir = "saveCache"
        mkdir(dir)
        val file = "$dir/cached.txt"
        val contents = "hello, world"
        writeFile(file, contents)
        val patterns = listOf("$dir/**")

        val primaryKey = "linux-gradle-feature/123123"

        cacheService {
            saveAndLog(patterns, primaryKey, "1-")

            fs2.promises.unlink(file)

            assertEquals(
                RestoreType.Exact(primaryKey),
                restoreAndLog(
                    patterns,
                    primaryKey,
                    restoreKeys = listOf("linux-gradle-", "linux-"),
                    version = "1-",
                ),
                "Cache restored from exact match",
            )

            assertEquals(
                readFile(file),
                contents,
                "Contents after restore should match",
            )

            assertEquals(
                RestoreType.Partial(primaryKey),
                restoreAndLog(
                    patterns,
                    "asdf$primaryKey",
                    restoreKeys = listOf("linux-gradle-", "linux-"),
                    version = "1-",
                ),
                "PK not found => restored from restoreKeys",
            )

            assertEquals(
                readFile(file),
                contents,
                "Contents after restore should match",
            )
        }
    }

    @Test
    fun layeredCacheTest() = runTest {
        val dir = "saveCache"
        mkdir(dir)
        val file = "$dir/cached.txt"
        val contents = "hello, world"
        writeFile(file, contents)
        val patterns = listOf("$dir/**")

        val primaryKey = "prefix-gradle-features/cool/123123"

        val cache = LayeredCache(
            "test-cache",
            "prefix-",
            primaryKey = primaryKey,
            restoreKeys = listOf(
                "prefix-gradle-",
                "prefix-",
            ),
            paths = patterns
        )

        cacheService {
            assertEquals(
                RestoreType.None,
                cache.restore(),
                "No data -> RestoreType.None"
            )

            cache.save()

            assertEquals(
                RestoreType.Exact(primaryKey),
                cache.restore(),
                "Restore after saving exact cache -> RestoreType.Exact"
            )
        }
    }
}
