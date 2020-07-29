package com.github.burrunan.gradle

import crypto.createHash
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

    for (name in fileNames) {
        if (fs.statSync(name).isDirectory()) {
            continue
        }
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
    hash.end()
    return hash.digest("hex")
}

