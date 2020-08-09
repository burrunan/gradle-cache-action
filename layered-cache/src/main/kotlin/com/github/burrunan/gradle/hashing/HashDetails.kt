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
package com.github.burrunan.gradle.hashing

import crypto.createHash
import fs.createReadStream
import fs2.promises.stat
import actions.glob.create
import com.github.burrunan.wrappers.nodejs.pipe
import com.github.burrunan.wrappers.nodejs.use
import kotlinx.coroutines.await
import kotlinx.serialization.Serializable
import process

@Serializable
class HashDetails(
    val info: HashInfo,
    val contents: HashContents,
)

@Serializable
class HashInfo(
    val totalBytes: Long,
    val hash: String,
    val totalFiles: Int,
)

@Serializable
class HashContents(
    val files: Map<String, FileDetails>,
)

@Serializable
class FileDetails(
    val fileSize: Long,
    val hash: String,
)

/**
 * Sample: ~/.gradle/caches/modules-2/files-2.1/com.google.errorprone/error_prone_annotations/2.0.18/5f65affce1684999e2f4024983835efc3504012e/error_prone_annotations-2.0.18.jar
 */
private fun sha1FromModulesFileName(key: String): String {
    val lastSlash = key.lastIndexOf('/')
    val hashStart = key.lastIndexOf('/', startIndex = lastSlash - 1) + 1
    return key.substring(hashStart, lastSlash).padStart(40, '0')
}

suspend fun hashFilesDetailed(vararg paths: String, algorithm: String = "sha1"): HashDetails {
    val globber = create(paths.joinToString("\n")).await()
    val fileNames = globber.glob().await()
    // Sorting is needed for stable overall hash
    fileNames.sort()

    val githubWorkspace = process.cwd()
    val homeDir = os.homedir()

    var totalBytes = 0L
    val files = mutableMapOf<String, FileDetails>()
    val overallHash = createHash(algorithm)
    for (name in fileNames) {
        val statSync = stat(name).await()
        if (statSync.isDirectory()) {
            continue
        }
        val fileSize = statSync.size.toLong()
        totalBytes += fileSize
        val key = when {
            name.startsWith(githubWorkspace) ->
                "ws://" + name.substring(githubWorkspace.length)
            name.startsWith(homeDir) ->
                "~" + name.substring(homeDir.length)
            else -> name
        }.replace('\\', '/')
        // Avoid hashing the contents when we know the hash from the file path
        val digest = when {
            algorithm == "sha1" && key.startsWith("~/.gradle/caches/modules-2/files-2.1/") ->
                sha1FromModulesFileName(key)
            key.startsWith("~/.gradle/caches/build-cache-1/") ->
                key.substringAfterLast('/')
            else -> {
                val hash = createHash(algorithm)
                createReadStream(name).use {
                    it.pipe(hash)
                }
                hash.digest().toString("hex")
            }
        }
        files[key] = FileDetails(fileSize, digest)
        // Add filename
        overallHash.update(key)
        overallHash.update(digest)
    }
    return HashDetails(
        HashInfo(totalBytes, overallHash.digest("hex"), files.size),
        HashContents(files),
    )
}
