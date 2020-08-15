@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package octokit.types

external interface EndpointDefaultsRequestHeader: RequestHeaders {
}

external interface EndpointDefaultsMediaType {
    var format: String
    var previews: Array<String>
}

external interface EndpointDefaults {
    var baseUrl: Url
    var method: String /* "DELETE" | "GET" | "HEAD" | "PATCH" | "POST" | "PUT" */
    var url: Url?
    var headers: EndpointDefaultsRequestHeader
    var mediaType: EndpointDefaultsMediaType
}
