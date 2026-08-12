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
package actions.glob

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
import node.process.process
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RemoveFilesTest {
    @Test
    fun removesAFileSharedByConcurrentCleanupsWithoutComplaining() = runTest {
        val dirName = "removeFilesConcurrentTest"
        mkdir(dirName)
        val file = path.join(dirName, "bc-entry")
        writeFile(file, "a", BufferEncoding.utf8)

        // The cache proxy stores every payload for a given entry id under the same path and cleans it
        // up from a detached coroutine, so cleanups race whenever an id is stored more than once. The
        // loser of that race finds the file already gone, which must not even be worth a warning.
        val warnings = capturingWarnings {
            coroutineScope {
                List(4) { async { removeFiles(listOf(file)) } }.awaitAll()
            }
        }

        assertFalse(existsSync(file), "$file should have been removed")
        assertEquals(emptyList(), warnings, "concurrent removals of $file should be silent")
    }

    @Test
    fun warnsRatherThanFailingWhenAFileCannotBeRemoved() = runTest {
        val dirName = "removeFilesUndeletableTest"
        val nested = path.join(dirName, "nested")
        mkdir(dirName)
        mkdir(nested)
        // rm refuses a directory unless recursive is set, and force does not cover that. The failure
        // must stay inside removeFiles: escaping, it would reach the uncaught-coroutine handler and
        // take the Node process, and the Gradle build it runs, down with it.
        val warnings = capturingWarnings {
            removeFiles(listOf(nested))
        }

        assertTrue(existsSync(nested), "$nested is a directory, so rm should have left it alone")
        assertEquals(1, warnings.size, "expected one warning, got $warnings")
        assertTrue(
            warnings[0].startsWith("::warning::Unable to remove 1 file, left in place: ") && warnings[0].contains("nested"),
            "the warning should name the file it could not remove: ${warnings[0]}",
        )
    }

    @Test
    fun reportsEveryFileItCouldNotRemoveInASingleWarning() = runTest {
        val dirName = "removeFilesManyUndeletableTest"
        mkdir(dirName)
        val nested = List(3) { path.join(dirName, "nested$it") }
        nested.forEach { mkdir(it) }
        // A cause such as a read-only mount fails every entry at once, and the callers hand whole file
        // lists over, so the log must not receive one copy of that failure per file.
        val warnings = capturingWarnings {
            removeFiles(listOf("$dirName/*"))
        }

        assertEquals(1, warnings.size, "the failures should be reported together, got $warnings")
        assertTrue(
            warnings[0].startsWith("::warning::Unable to remove 3 files, left in place: "),
            "the warning should count the failures: ${warnings[0]}",
        )
        for (dir in nested) {
            assertTrue(warnings[0].contains(path.basename(dir)), "$dir is missing from ${warnings[0]}")
        }
    }
}

/**
 * Collects the workflow commands [actions.core.warning] prints while [block] runs.
 *
 * The action's warnings go to stdout as `::warning::...` lines, and there is no other way to observe
 * that a cleanup swallowed a failure rather than never hitting one.
 */
private suspend fun capturingWarnings(block: suspend () -> Unit): List<String> {
    val stdout = process.stdout.asDynamic()
    val write = stdout.write
    val captured = StringBuilder()
    stdout.write = { chunk: Any? ->
        captured.append(chunk.toString())
        true
    }
    try {
        block()
    } finally {
        stdout.write = write
    }
    return captured.lines().filter { it.startsWith("::warning::") }
}
