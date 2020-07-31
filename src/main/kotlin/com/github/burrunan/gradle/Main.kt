package com.github.burrunan.gradle

import com.github.burrunan.gradle.github.event.currentTrigger
import github.actions.core.getInput

suspend fun main() {
    val params = Parameters(
        jobId = getInput("job-id", jsObject { required = false }),
        path = getInput("path", jsObject { required = false }),
        debug = getInput("debug", jsObject { required = false }).toBoolean(),
    )

    GradleCacheAction(currentTrigger(), params).run()
}
