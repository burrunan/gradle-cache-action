@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS",
    "DEPRECATION")
package octokit.types

external interface RequestHeaders {
    var accept: String?
    var authorization: String?
    @nativeGetter
    operator fun get(header: String): Any? /* String? | Number? */
    @nativeSetter
    operator fun set(header: String, value: String?)
    @nativeSetter
    operator fun set(header: String, value: Number?)
}
