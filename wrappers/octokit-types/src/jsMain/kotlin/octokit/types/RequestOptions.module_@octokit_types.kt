@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package octokit.types

external interface RequestOptions {
    var method: String /* "DELETE" | "GET" | "HEAD" | "PATCH" | "POST" | "PUT" */
    var url: Url
    var headers: RequestHeaders
    var body: Any?
    var request: RequestRequestOptions?
}
