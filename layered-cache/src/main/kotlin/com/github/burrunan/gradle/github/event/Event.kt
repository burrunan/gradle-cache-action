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

external interface Event {
    var repository: Repository
}

/**
 * See https://docs.github.com/en/developers/webhooks-and-events/webhook-events-and-payloads#push
 */
external interface BranchPushEvent : Event {
    var action: String
    var ref: String
    var commits: List<Commit>
    var head_commit: Commit
    var pusher: UserInfo
    var compare: String
    var created: Boolean
    var deleted: Boolean
    var forced: Boolean
}

/**
 * See https://docs.github.com/en/developers/webhooks-and-events/webhook-events-and-payloads#pull_request
 */
external interface PullRequestEvent : Event {
    var url: String
    var id: Number
    var number: Int
    var state: String
    var base_ref: String
    var locked: Boolean
    var title: String
    var body: String
    var pull_request: PullRequest
}

external interface Commit {
    var author: UserInfo
    var committer: UserInfo
    var id: String
    var message: String
    var tree_id: String
    var url: String
}

external interface User : UserInfo

external interface UserInfo {
    var email: String
    var name: String?
    var username: String
}

external interface Repository {
    var fork: Boolean
    var master_branch: String
    var default_branch: String
    var name: String
    var organization: String
}

external interface PullRequest {
    var number: Int
    var additions: Int
    var deletions: Int
    var commits: Int
    var draft: Boolean
    var changed_files: Int
    var base: PullRequestBase
    var head: PullRequestHead
    var repo: Repository
    var user: User
}

external interface CommitRef {
    var label: String
    var ref: String
    var sha: String
}

external interface PullRequestBase : CommitRef

external interface PullRequestHead : CommitRef
