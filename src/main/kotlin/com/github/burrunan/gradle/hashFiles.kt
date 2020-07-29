package com.github.burrunan.gradle

import crypto.createHash
import github.actions.core.info
import github.actions.glob.create
import kotlinx.coroutines.await
import process

suspend fun hashFiles(vararg paths: String): String {
    val globber = create(paths.joinToString("\n")).await()
    val fileNames = globber.glob().await()
    fileNames.sort()

    val githubWorkspace = process.cwd()
    val homeDir = os.homedir()
    val hash = createHash("sha256")

    var total = 0.0
    for (name in fileNames) {
        val statSync = fs.statSync(name)
        if (statSync.isDirectory()) {
            continue
        }
        total += statSync.size.toDouble()
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
    info("Found ${fileNames.size} files, $total bytes")
    hash.end()
    return hash.digest("hex")
}

