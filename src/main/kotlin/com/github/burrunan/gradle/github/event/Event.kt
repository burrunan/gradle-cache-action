package com.github.burrunan.gradle.github.event

external interface Event {
    var after: String
    var before: String
    var repository: Repository
}

/**
 * See https://developer.github.com/webhooks/event-payloads/#push
 */
external interface BranchPushEvent: Event {
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
 * See https://developer.github.com/webhooks/event-payloads/#pull_request
 */
external interface PullRequestEvent: Event {
    var base_ref: String
    var pull_request: PullRequest
    var number: Int
}

external interface Commit {
    var author: UserInfo
    var committer: UserInfo
    var id: String
    var message: String
    var tree_id: String
    var url: String
}

external interface User: UserInfo {
}

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

external interface PullRequestBase: CommitRef {
}

external interface PullRequestHead: CommitRef {
}
