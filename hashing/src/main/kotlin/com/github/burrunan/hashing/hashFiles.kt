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
package com.github.burrunan.hashing

import actions.core.ActionFailedException
import actions.core.warning
import actions.glob.Globber
import actions.glob.glob
import com.github.burrunan.wrappers.nodejs.normalizedPath
import com.github.burrunan.wrappers.nodejs.pipe
import com.github.burrunan.wrappers.nodejs.use
import crypto.createHash
import process

data class HashResult(
    val hash: String,
    val numFiles: Int,
    val totalBytes: Int,
)

suspend fun hashFiles(
    vararg paths: String,
    algorithm: String = "sha1",
    includeFileName: Boolean = true,
): HashResult = try {
    val globber = Globber(paths.joinToString("\n"))
    val fileNames = globber.glob()
    fileNames.sort()

    val githubWorkspace = process.cwd()
    val homeDir = "~".normalizedPath
    val hash = createHash(algorithm)

    var totalBytes = 0
    var numFiles = 0
    for (name in fileNames) {
        val statSync = fs2.promises.stat(name)
        if (statSync.isDirectory()) {
            continue
        }
        val key = when {
            name.startsWith(githubWorkspace) ->
                "ws://" + name.substring(githubWorkspace.length)
            name.startsWith(homeDir) ->
                "~" + name.substring(homeDir.length)
            else -> name
        }.replace('\\', '/')

        numFiles += 1
        totalBytes += statSync.size.toInt()
        // Add filename

        try {
            fs.createReadStream(name).use {
                it.pipe(hash, end = false)
            }
        } catch (e: Throwable) {
            warning("Unable to hash $name, will ignore the file: ${e.stackTraceToString()}")
            continue
        }

        if (includeFileName) {
            hash.update(key, "utf8")
        }
    }
    hash.end()
    HashResult(
        hash = hash.digest("hex"),
        numFiles = numFiles,
        totalBytes = totalBytes,
    )
} catch (e: Throwable) {
    throw ActionFailedException("Unable to hash ${paths.joinToString(", ")}: $e", e)
}
