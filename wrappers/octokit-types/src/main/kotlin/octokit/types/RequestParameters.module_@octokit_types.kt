@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS",
    "DEPRECATION")
package octokit.types

external interface `T$8` {
    var format: String?
    var previews: Array<String>?
}

external interface RequestParameters {
    var baseUrl: Url?
    var headers: RequestHeaders?
    var mediaType: `T$8`?
    var request: RequestRequestOptions?
    @nativeGetter
    operator fun get(parameter: String): Any?
    @nativeSetter
    operator fun set(parameter: String, value: Any)
}
