package com.github.burrunan.gradle

import com.github.burrunan.gradle.github.formatBytes
import com.github.burrunan.gradle.hashing.hashFilesDetailed
import kotlinx.coroutines.await
import runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GlobTest {
    @Test
    fun glob() = runTest {
        val dirName = "globTest"
        if (!exists(dirName)) {
            fs2.promises.mkdir(dirName).await()
        }
        fs2.promises.writeFile("$dirName/good.txt", "a", "utf8")
        fs2.promises.writeFile("$dirName/bad.txt", "a", "utf8")

        val hash = hashFilesDetailed(
            "**/*.txt",
            "!**/*bad**",
        )
        println("${hash.info.totalBytes.formatBytes()} ${hash.info.totalFiles} files")
        val actual = hash.contents.files.entries.joinToString { (file, details) ->
            "${details.fileSize.formatBytes()} ${details.hash} $file"
        }
        assertEquals("1 B 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8 ws:///globTest/good.txt", actual)
    }
}
