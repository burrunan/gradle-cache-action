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

import crypto.createHash
import actions.glob.create
import com.github.burrunan.wrappers.nodejs.pipe
import com.github.burrunan.wrappers.nodejs.use
import kotlinx.coroutines.await
import process

data class HashResult(
    val hash: String,
    val numFiles: Int,
    val totalBytes: Int,
)

suspend fun hashFiles(vararg paths: String, algorithm: String = "sha1"): HashResult = try {
    val globber = create(paths.joinToString("\n")).await()
    val fileNames = globber.glob().await()
    fileNames.sort()

    val githubWorkspace = process.cwd()
    val homeDir = os.homedir()
    val hash = createHash(algorithm)

    var totalBytes = 0
    var numFiles = 0
    for (name in fileNames) {
        val statSync = fs2.promises.stat(name).await()
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
        hash.update(key, "utf8")

        fs.createReadStream(name).use {
            it.pipe(hash, end = false)
        }
    }
    hash.end()
    HashResult(
        hash = hash.digest("hex"),
        numFiles = numFiles,
        totalBytes = totalBytes,
    )
} catch (e: Throwable) {
    throw IllegalArgumentException("Unable to hash ${paths.joinToString(", ")}", e)
}
