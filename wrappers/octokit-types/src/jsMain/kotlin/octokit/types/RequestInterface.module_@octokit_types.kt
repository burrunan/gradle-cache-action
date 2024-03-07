@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS",
    "DEPRECATION")
package octokit.types

import kotlin.js.Promise

external interface RequestInterface<D : Any?> {
    @nativeInvoke
    operator fun <T, O : RequestParameters> invoke(options: O /* O & `T$4` & Any */): Promise<OctokitResponse<T>>
    @nativeInvoke
    operator fun invoke(route: Any, options: Any = definedExternally): Any
    @nativeInvoke
    operator fun <@Suppress("FINAL_UPPER_BOUND") R : Route> invoke(route: R, options: Any = definedExternally): Any
//    var defaults: (newDefaults: O) -> RequestInterface<D /* D & O */>
    var defaults: RequestInterfaceDefaults<D>
    var endpoint: EndpointInterface<D>
}

external interface RequestInterface__0 : RequestInterface<Any?>

external interface RequestInterfaceDefaults<D> {
    @nativeInvoke
    operator fun<O: RequestParameters> invoke(newDefaults: O): RequestInterface<D /* D & O */>
}
