package com.github.burrunan.gradle

import github.actions.core.info
import github.actions.core.warning
import process

const val RefKey = "GITHUB_REF"

fun isValidEvent() = !process.env[RefKey].isNullOrBlank()

suspend fun main() {
    if (!isValidEvent()) {
        warning("$RefKey is needed for the action to work. GitHub event was ${process.env["GITHUB_EVENT_NAME"]}")
        return
    }
    val s = process.env["GITHUB_EVENT_PATH"]
    info("ref: ${process.env[RefKey]}")
    info("event path: $s")
    info("event: " + fs.readFileSync(s ?: "unknown"))

    GradleCacheAction(process.env[RefKey] ?: "").run()
}
