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

import actions.core.warning
import js.objects.unsafeJso
import js.promise.await
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import node.fs.RmOptions
import node.fs.rm

suspend fun removeFiles(files: List<String>) {
    if (files.isEmpty()) {
        return
    }
    val globber = create(files.joinToString("\n"))
    val fileNames = globber.glob().await()
    val failures = mutableListOf<String>()
    supervisorScope {
        for (file in fileNames) {
            launch {
                try {
                    // Concurrent cleanups can share a path, so a file may already be gone by the time
                    // this coroutine runs. force ignores that, whereas unlink would reject with ENOENT.
                    rm(file, unsafeJso<RmOptions> { force = true })
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Throwable) {
                    // supervisorScope keeps the sibling deletes running, yet the failure still reaches
                    // the uncaught-coroutine handler, which terminates the Node process along with the
                    // Gradle build. Node's fs messages name the path, so the message identifies the file.
                    failures += e.message ?: file
                }
            }
        }
    }
    if (failures.isNotEmpty()) {
        // The callers pass whole file lists, and a cause like a read-only mount fails every entry,
        // so one warning per file would bury the log under copies of a single failure.
        val files = if (failures.size == 1) "file" else "files"
        warning(
            failures.joinToString(", ", "Unable to remove ${failures.size} $files, left in place: ", limit = 5),
        )
    }
}
