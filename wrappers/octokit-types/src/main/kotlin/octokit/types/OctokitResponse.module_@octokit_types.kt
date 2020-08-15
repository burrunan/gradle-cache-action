@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package octokit.types

external interface OctokitResponse<T> {
    var headers: ResponseHeaders
    var status: Number
    var url: Url
    var data: T
}
