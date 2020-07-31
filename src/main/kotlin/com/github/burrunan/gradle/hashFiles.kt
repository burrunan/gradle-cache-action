package com.github.burrunan.gradle

import crypto.createHash
import github.actions.core.info
import github.actions.glob.create
//import github.actions.glob.create
import kotlinx.coroutines.await
import process

suspend fun hashFiles(vararg paths: String): HashResult {
    val globber = create(paths.joinToString("\n")).await()
    val fileNames = globber.glob().await()
    fileNames.sort()

    val githubWorkspace = process.cwd()
    val homeDir = os.homedir()
    val hash = createHash("sha256")

    var totalBytes = 0
    var numFiles = 0
    for (name in fileNames) {
        val statSync = fs.statSync(name)
        if (statSync.isDirectory()) {
            continue
        }
        numFiles += 1
        totalBytes += statSync.size.toInt()
        info("${statSync.size} $name")
        val key = when {
            name.startsWith(githubWorkspace) ->
                "ws://" + name.substring(githubWorkspace.length)
            name.startsWith(homeDir) ->
                "~" + name.substring(homeDir.length)
            else -> name
        }
        // Add filename
        hash.update(key, "utf8")

        fs.createReadStream(name).use {
            it.pipe(hash, end = false)
        }
    }
    info("Found ${fileNames.size} files, $totalBytes bytes")
    hash.end()
    return HashResult(
        hash = hash.digest("hex"),
        numFiles = numFiles,
        totalBytes = totalBytes,
    )
}

