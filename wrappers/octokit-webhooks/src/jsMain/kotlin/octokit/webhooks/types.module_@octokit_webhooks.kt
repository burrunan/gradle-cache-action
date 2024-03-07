@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

//import tsstdlib.IterableIterator

external interface WebhookEvent<T> {
    var id: String
    var name: String /* "check_run" | "check_suite" | "commit_comment" | "content_reference" | "create" | "delete" | "deploy_key" | "deployment" | "deployment_status" | "fork" | "github_app_authorization" | "gollum" | "installation" | "installation_repositories" | "issue_comment" | "issues" | "label" | "marketplace_purchase" | "member" | "membership" | "meta" | "milestone" | "organization" | "org_block" | "package" | "page_build" | "ping" | "project_card" | "project_column" | "project" | "public" | "pull_request" | "pull_request_review" | "pull_request_review_comment" | "push" | "release" | "repository_dispatch" | "repository" | "repository_import" | "repository_vulnerability_alert" | "security_advisory" | "sponsorship" | "star" | "status" | "team" | "team_add" | "watch" | "workflow_dispatch" | "workflow_run" */
    var payload: T
}

external interface WebhookEvent__0 : WebhookEvent<Any>

external interface Options<T : WebhookEvent__0> {
    var path: String?
    var secret: String?
    var transform: TransformMethod<T>?
}

typealias TransformMethod<@Suppress("UNUSED_TYPEALIAS_PARAMETER") T> = (event: WebhookEvent__0) -> dynamic

external interface `T$74` {
    var event: WebhookEvent__0
}

//external interface WebhookEventHandlerError : AggregateError<Error /* Error & Any & `T$74` */> {
//    var event: WebhookEvent__0
//    var errors: Array<Error /* Error & Any & `T$74` */>
//}
//
//open external class AggregateError<T: Error> : tsstdlib.Iterable<T> {
//    fun iterator(): IterableIterator<T>
//}
