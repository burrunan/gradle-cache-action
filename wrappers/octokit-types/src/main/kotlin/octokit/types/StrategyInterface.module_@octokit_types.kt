@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS",
    "DEPRECATION")
package octokit.types

external interface StrategyInterface<StrategyOptions : Array<Any>, AuthOptions : Array<Any>, Authentication : Any?> {
    @nativeInvoke
    operator fun invoke(vararg args: StrategyOptions): AuthInterface<AuthOptions, Authentication>
}
