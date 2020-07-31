package com.github.burrunan.gradle.github.event

import com.github.burrunan.gradle.github.env.ActionsEnvironment

sealed class ActionsTrigger(val name: String, open val event: Event) {
    class PullRequest(override val event: PullRequestEvent): ActionsTrigger("pull_request", event)
    class BranchPush(override val event: BranchPushEvent): ActionsTrigger("push", event)
    class Other(name: String, event: Event): ActionsTrigger(name, event)
}

fun currentTrigger(): ActionsTrigger {
    val eventString = fs.readFileSync(ActionsEnvironment.GITHUB_EVENT_PATH, "utf8")
    val event = JSON.parse<Event>(eventString)
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    return when (val eventName = ActionsEnvironment.GITHUB_EVENT_NAME) {
        "pull_request" -> ActionsTrigger.PullRequest(event as PullRequestEvent)
        "push" -> ActionsTrigger.BranchPush(event as BranchPushEvent)
        else -> ActionsTrigger.Other(eventName, event)
    }
}
