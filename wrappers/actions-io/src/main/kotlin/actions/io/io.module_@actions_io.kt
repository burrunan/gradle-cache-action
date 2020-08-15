@file:JsModule("@actions/io")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package actions.io

import kotlin.js.Promise

external interface CopyOptions {
    var recursive: Boolean?
    var force: Boolean?
}

external interface MoveOptions {
    var force: Boolean?
}

@JsName("cp")
external fun cpAsync(source: String, dest: String, options: CopyOptions = definedExternally): Promise<Unit>

@JsName("mv")
external fun mvAsync(source: String, dest: String, options: MoveOptions = definedExternally): Promise<Unit>

@JsName("rmRF")
external fun rmRFAsync(inputPath: String): Promise<Unit>

@JsName("mkdirP")
external fun mkdirPAsync(fsPath: String): Promise<Unit>

@JsName("which")
external fun whichAsync(tool: String, check: Boolean = definedExternally): Promise<String>
