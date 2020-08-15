@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package octokit.types

external interface `T$7` {
    var resource: String
    var code: String
    var field: String
    var message: String?
}

external interface RequestError {
    var name: String
    var status: Number
    var documentation_url: String
    var errors: Array<`T$7`>?
}
