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
package com.github.burrunan.gradle.github.event

import com.github.burrunan.gradle.GradleCacheAction
import com.github.burrunan.gradle.github.env.ActionsEnvironment
import fs2.promises.readFile
import kotlinx.coroutines.await

sealed class ActionsTrigger(val name: String, open val event: Event) {
    class PullRequest(override val event: PullRequestEvent) : ActionsTrigger("pull_request", event)
    class BranchPush(override val event: BranchPushEvent) : ActionsTrigger("push", event)
    class Schedule(name: String, event: Event) : ActionsTrigger(name, event)
    class WorkflowDispatch(name: String, event: Event) : ActionsTrigger(name, event)
    class Other(name: String, event: Event) : ActionsTrigger(name, event)
}

val ActionsTrigger.cacheKey: String
    get() = when (this) {
        is ActionsTrigger.PullRequest -> "PR${event.pull_request.number}"
        is ActionsTrigger.BranchPush -> when (val ref = event.ref.removePrefix("refs/heads/")) {
            event.repository.default_branch.removePrefix("refs/heads/") ->
                GradleCacheAction.DEFAULT_BRANCH_VAR
            else -> ref
        }
        is ActionsTrigger.Schedule, is ActionsTrigger.WorkflowDispatch ->
            GradleCacheAction.DEFAULT_BRANCH_VAR
        is ActionsTrigger.Other -> "$name-${ActionsEnvironment.GITHUB_WORKFLOW}-${ActionsEnvironment.GITHUB_SHA}"
    }

suspend fun currentTrigger(): ActionsTrigger {
    val eventString = readFile(ActionsEnvironment.GITHUB_EVENT_PATH, "utf8").await()
    val event = JSON.parse<Event>(eventString)
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    return when (val eventName = ActionsEnvironment.GITHUB_EVENT_NAME) {
        "pull_request" -> ActionsTrigger.PullRequest(event as PullRequestEvent)
        "push" -> ActionsTrigger.BranchPush(event as BranchPushEvent)
        "schedule" -> ActionsTrigger.Schedule(eventName, event)
        "workflow_dispatch" -> ActionsTrigger.WorkflowDispatch(eventName, event)
        else -> ActionsTrigger.Other(eventName, event)
    }
}
