@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS",
    "DEPRECATION")
package octokit.types

external interface RequestRequestOptions {
    var agent: Any?
    var fetch: Fetch?
    var signal: Signal?
    var timeout: Number?
    @nativeGetter
    operator fun get(option: String): Any?
    @nativeSetter
    operator fun set(option: String, value: Any)
}
