@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package github.actions.exec

import Buffer
import NodeJS.ProcessEnv
import kotlin.js.*
import kotlin.js.Json
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*
import stream.Writable

external interface ExecListeners {
    var stdout: ((data: Buffer) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
    var stderr: ((data: Buffer) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
    var stdline: ((data: String) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
    var errline: ((data: String) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
    var debug: ((data: String) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ExecOptions {
    var cwd: String?
        get() = definedExternally
        set(value) = definedExternally
    var env: ProcessEnv?
        get() = definedExternally
        set(value) = definedExternally
    var silent: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var outStream: Writable?
        get() = definedExternally
        set(value) = definedExternally
    var errStream: Writable?
        get() = definedExternally
        set(value) = definedExternally
    var windowsVerbatimArguments: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var failOnStdErr: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ignoreReturnCode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var delay: Number?
        get() = definedExternally
        set(value) = definedExternally
    var input: Buffer?
        get() = definedExternally
        set(value) = definedExternally
    var listeners: ExecListeners?
        get() = definedExternally
        set(value) = definedExternally
}
