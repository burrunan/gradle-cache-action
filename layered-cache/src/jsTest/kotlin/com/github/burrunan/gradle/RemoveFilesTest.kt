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
package com.github.burrunan.gradle

import actions.glob.removeFiles
import com.github.burrunan.test.runTest
import com.github.burrunan.wrappers.nodejs.mkdir
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import node.buffer.BufferEncoding
import node.buffer.utf8
import node.fs.existsSync
import node.fs.writeFile
import node.path.path
import kotlin.test.Test
import kotlin.test.assertFalse

class RemoveFilesTest {
    @Test
    fun removesFilesConcurrentlyWithoutFailingOnAlreadyRemovedOnes() = runTest {
        val dirName = "removeFilesTest"
        mkdir(dirName)
        val file = path.join(dirName, "bc-entry")
        writeFile(file, "a", BufferEncoding.utf8)

        // The cache proxy stores every payload for a given entry id under the same path and cleans
        // it up from a detached coroutine, so cleanups race whenever an id is stored more than once.
        coroutineScope {
            List(4) { async { removeFiles(listOf(file)) } }.awaitAll()
        }

        assertFalse(existsSync(file), "$file should have been removed")
    }
}
