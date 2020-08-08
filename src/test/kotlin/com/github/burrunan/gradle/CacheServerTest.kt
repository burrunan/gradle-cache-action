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

import com.github.burrunan.gradle.cache.CacheService
import com.github.burrunan.gradle.cache.RestoreType
import github.actions.cache.restoreAndLog
import github.actions.cache.saveAndLog
import kotlinx.coroutines.await
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
        fs2.promises.writeFile(file, contents, "utf8").await()
        val patterns = listOf("$dir/**")

        val primaryKey = "linux-gradle-123123"

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
                fs2.promises.readFile(file, "utf8").await(),
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
                fs2.promises.readFile(file, "utf8").await(),
                contents,
                "Contents after restore should match",
            )
        }
    }
}
