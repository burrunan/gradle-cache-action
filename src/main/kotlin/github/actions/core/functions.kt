@file:JsModule("@actions/core")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package github.actions.core

import kotlin.js.Promise

external interface InputOptions {
    var required: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external enum class ExitCode {
    Success /* = 0 */,
    Failure /* = 1 */
}

external fun exportVariable(name: String, param_val: Any)

external fun setSecret(secret: String)

external fun addPath(inputPath: String)

external fun getInput(name: String, options: InputOptions = definedExternally): String

external fun setOutput(name: String, value: Any)

external fun setCommandEcho(enabled: Boolean)

external fun setFailed(message: String)

external fun setFailed(message: Error)

external fun isDebug(): Boolean

external fun debug(message: String)

external fun error(message: String)

external fun error(message: Error)

external fun warning(message: String)

external fun warning(message: Error)

external fun info(message: String)

external fun startGroup(name: String)

external fun endGroup()

external fun <T> group(name: String, fn: () -> Promise<T>): Promise<T>

external fun saveState(name: String, value: Any)

external fun getState(name: String): String
