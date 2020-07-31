@file:JsQualifier("events.global.NodeJS")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package events.global.NodeJS

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

external interface EventEmitter {
    fun addListener(event: String, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun addListener(event: Any, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun on(event: String, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun on(event: Any, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun once(event: String, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun once(event: Any, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun removeListener(event: String, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun removeListener(event: Any, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun off(event: String, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun off(event: Any, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun removeAllListeners(event: String = definedExternally): EventEmitter /* this */
    fun removeAllListeners(event: Any = definedExternally): EventEmitter /* this */
    fun setMaxListeners(n: Number): EventEmitter /* this */
    fun getMaxListeners(): Number
    fun listeners(event: String): Array<Function<*>>
    fun listeners(event: Any): Array<Function<*>>
    fun rawListeners(event: String): Array<Function<*>>
    fun rawListeners(event: Any): Array<Function<*>>
    fun emit(event: String, vararg args: Any): Boolean
    fun emit(event: Any, vararg args: Any): Boolean
    fun listenerCount(type: String): Number
    fun listenerCount(type: Any): Number
    fun prependListener(event: String, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun prependListener(event: Any, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun prependOnceListener(event: String, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun prependOnceListener(event: Any, listener: (args: Any) -> Unit): EventEmitter /* this */
    fun eventNames(): Array<dynamic /* String | Any */>
}