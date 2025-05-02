@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS",
    "DEPRECATION")
package octokit.types

external interface ResponseHeaders {
    var date: String?
    var etag: String?
    var link: String?
    var location: String?
    var server: String?
    var status: String?
    var vary: String?
    @nativeGetter
    operator fun get(header: String): Any? /* String? | Number? */
    @nativeSetter
    operator fun set(header: String, value: String?)
    @nativeSetter
    operator fun set(header: String, value: Number?)
}
