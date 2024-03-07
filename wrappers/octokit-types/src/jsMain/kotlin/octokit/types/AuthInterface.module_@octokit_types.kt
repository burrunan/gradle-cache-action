@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS",
    "DEPRECATION")
package octokit.types

import kotlin.js.Promise

external interface AuthInterfaceHook {
    @nativeInvoke
    operator fun <T> invoke(request: RequestInterface__0, options: RequestParameters /* RequestParameters & `T$6` */): Promise<OctokitResponse<T>>
    @nativeInvoke
    operator fun <T> invoke(request: RequestInterface__0, route: Route, parameters: RequestParameters = definedExternally): Promise<OctokitResponse<T>>
}

external interface AuthInterface<AuthOptions : Array<Any>, Authentication : Any?> {
    @nativeInvoke
    operator fun invoke(vararg args: AuthOptions): Promise<Authentication>
    var hook: AuthInterfaceHook
}
