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

import com.github.burrunan.formatBytes

class Diff(
    val newFiles: Int,
    val totalUpdated: Long,
    val totalDeleted: Long,
    val messages: List<String>,
    val updatedFiles: List<String>,
    val deletedFiles: List<String>,
) {
    val summary: String
        get() =
            (if (updatedFiles.isNotEmpty()) "${updatedFiles.size} updates (${totalUpdated.formatBytes()})" else "") +
                (if (deletedFiles.isNotEmpty()) (if (updatedFiles.isNotEmpty()) ", " else "") +
                    "${deletedFiles.size} deletes (${totalDeleted.formatBytes()})" else "") +
                "\n  " + messages.joinToString("\n  ")
}

fun diff(
    oldContents: HashContents,
    newContents: HashContents,
    maxUpdatesToPrint: Int = 50,
): Diff {
    val messages = mutableListOf<String>()
    val updatedFiles = mutableListOf<String>()
    val deletedFiles = mutableListOf<String>()
    var newFiles = 0
    var totalUpdated = 0L
    var totalDeleted = 0L
    for ((file, hash) in newContents.files) {
        val oldHash = oldContents.files[file]
        if (oldHash?.hash == hash.hash) {
            continue
        }
        updatedFiles.add(file)
        if (messages.size >= maxUpdatesToPrint) {
            continue
        }
        messages.add(
            if (oldHash == null) {
                newFiles += 1
                totalUpdated += hash.fileSize
                "N ${hash.fileSize} $file ${hash.hash}"
            } else {
                totalUpdated += hash.fileSize
                "U ${hash.fileSize} $file ${oldHash.fileSize} ${oldHash.hash} => ${hash.hash}"
            },
        )
    }
    if (oldContents.files.size + newFiles != newContents.files.size) {
        for ((file, hash) in oldContents.files) {
            if (file !in newContents.files) {
                deletedFiles.add(file)
                totalDeleted += hash.fileSize
                if (messages.size < maxUpdatesToPrint) {
                    messages.add("D $file $hash")
                }
            }
        }
    }
    return Diff(
        newFiles = newFiles,
        totalUpdated = totalUpdated,
        totalDeleted = totalDeleted,
        messages = messages,
        updatedFiles = updatedFiles,
        deletedFiles = deletedFiles,
    )
}
