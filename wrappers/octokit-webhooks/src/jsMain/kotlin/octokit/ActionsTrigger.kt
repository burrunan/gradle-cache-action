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
package octokit

import actions.core.ActionsEnvironment
import node.buffer.BufferEncoding
import node.fs.readFile
import octokit.webhooks.WebhookPayloadPullRequest
import octokit.webhooks.WebhookPayloadPush
import octokit.webhooks.WebhookPayloadWorkflowDispatch

sealed class ActionsTrigger(val name: String, open val event: Any) {
    class PullRequest(override val event: WebhookPayloadPullRequest) : ActionsTrigger("pull_request", event)
    class BranchPush(override val event: WebhookPayloadPush) : ActionsTrigger("push", event)
    class WorkflowDispatch(override val event: WebhookPayloadWorkflowDispatch) : ActionsTrigger("workflow_dispatch", event)
    class Schedule(name: String, event: Any) : ActionsTrigger(name, event)
    class Other(name: String, event: Any) : ActionsTrigger(name, event)
}

suspend fun currentTrigger(): ActionsTrigger {
    val eventString = readFile(ActionsEnvironment.GITHUB_EVENT_PATH, BufferEncoding.utf8)
    val event = JSON.parse<Any>(eventString)
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    return when (val eventName = ActionsEnvironment.GITHUB_EVENT_NAME) {
        "pull_request" -> ActionsTrigger.PullRequest(event as WebhookPayloadPullRequest)
        "push" -> ActionsTrigger.BranchPush(event as WebhookPayloadPush)
        "workflow_dispatch" -> ActionsTrigger.WorkflowDispatch(event as WebhookPayloadWorkflowDispatch)
        "schedule" -> ActionsTrigger.Schedule(eventName, event)
        else -> ActionsTrigger.Other(eventName, event)
    }
}
