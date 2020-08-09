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

import com.github.burrunan.formatBytes
import com.github.burrunan.hashing.hashFilesDetailed
import com.github.burrunan.test.runTest
import com.github.burrunan.wrappers.nodejs.mkdir
import fs2.promises.writeFile
import path.path
import kotlin.test.Test
import kotlin.test.assertEquals

class GlobTest {
    @Test
    fun glob() = runTest {
        val dirName = "globTest"
        val dotGradle = path.join(dirName, ".gradle")
        mkdir(dirName)
        mkdir(dotGradle)
        writeFile(path.join(dirName, "settings.gradle"), "a")
        writeFile(path.join(dirName, "good.txt"), "a")
        writeFile(path.join(dirName, "bad.txt"), "a")
        writeFile(path.join(dotGradle, "extra.txt"), "a")

        val hash = hashFilesDetailed(
            "$dirName/**/*.gradle",
            "$dirName/**/*.txt",
            "!$dirName/**/.gradle/",
            "!$dirName/**/*bad**",
        )
        val actual = hash.contents.files.entries.joinToString("\n") { (file, details) ->
            "${details.fileSize.formatBytes()} ${details.hash} $file"
        }
        assertEquals(
            """
            1 B 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8 ws:///globTest/good.txt
            1 B 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8 ws:///globTest/settings.gradle
            """.trimIndent(),
            actual,
        )
    }
}
