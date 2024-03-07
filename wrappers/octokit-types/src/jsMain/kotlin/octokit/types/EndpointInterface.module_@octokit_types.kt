@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS",
    "DEPRECATION")
package octokit.types

external interface `T$4` {
    var method: String?
}

external interface MergeFunction<D> {
    @nativeInvoke
    operator fun <P : RequestParameters> invoke(route: Any, parameters: P = definedExternally): D /* D & Any & P */
    @nativeInvoke
    operator fun <@Suppress("FINAL_UPPER_BOUND") R : Route, P : RequestParameters> invoke(route: R, parameters: P = definedExternally): D /* D & Any & P */
    @nativeInvoke
    operator fun <P : RequestParameters> invoke(options: P): RequestParameters /* RequestParameters & `T$3` */
    @nativeInvoke
    operator fun invoke(): D /* D & RequestParameters */
}

external interface EndpointInterface<D : Any?> {
    @nativeInvoke
    operator fun <O : RequestParameters> invoke(options: O /* O & `T$4` & Any */): RequestOptions /* RequestOptions & Pick<D /* D & O */, Any> */
    @nativeInvoke
    operator fun <P : RequestParameters> invoke(route: Any, parameters: P = definedExternally): Any /* Any & Any */
    @nativeInvoke
    operator fun <@Suppress("FINAL_UPPER_BOUND") R : Route, P : RequestParameters> invoke(route: R, parameters: P = definedExternally): Any /* Any & Any */
    var DEFAULTS: D /* D & RequestParameters */
//    var defaults: (newDefaults: O) -> EndpointInterface<D /* D & O */>
    var defaults: EndpointInterfaceDefaults<D>
    var merge: MergeFunction<Any?>
    var parse: EndpointInterfaceOptionParser
}

external interface EndpointInterfaceDefaults<D> {
    @nativeInvoke
    operator fun<O: RequestParameters> invoke(newDefaults: O): EndpointInterface<D /* D & O */>
}

external interface EndpointInterfaceOptionParser {
    @nativeInvoke
    operator fun<O: RequestOptions> invoke(options: O): RequestOptions /*& Pick<O, keyof RequestOptions>*/
}
