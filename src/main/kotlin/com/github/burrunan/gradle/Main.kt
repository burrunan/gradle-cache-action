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
    info(process.env["GITHUB_EVENT_PATH"] ?: "no event")
    GradleCacheAction(process.env[RefKey] ?: "").run()
}
