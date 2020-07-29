@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package tsstdlib

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
import NodeJS.CallSite
import SharedArrayBuffer

external interface FunctionConstructor {
    @nativeInvoke
    operator fun invoke(vararg args: String): Function<*>
    var prototype: Function<*>
}

external interface ErrorConstructor {
    fun captureStackTrace(targetObject: Any?, constructorOpt: Function<*> = definedExternally)
    var prepareStackTrace: ((err: Error, stackTraces: Array<CallSite>) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var stackTraceLimit: Number
    @nativeInvoke
    operator fun invoke(message: String = definedExternally): Error
    var prototype: Error
}

external interface ConcatArray<T> {
    var length: Number
    @nativeGetter
    operator fun get(n: Number): T?
    @nativeSetter
    operator fun set(n: Number, value: T)
    fun join(separator: String = definedExternally): String
    fun slice(start: Number = definedExternally, end: Number = definedExternally): Array<T>
}

external interface ArrayConstructor {
    fun <T> from(iterable: Iterable<T>): Array<T>
    fun <T> from(iterable: ArrayLike<T>): Array<T>
    fun <T, U> from(iterable: Iterable<T>, mapfn: (v: T, k: Number) -> U, thisArg: Any = definedExternally): Array<U>
    fun <T, U> from(iterable: ArrayLike<T>, mapfn: (v: T, k: Number) -> U, thisArg: Any = definedExternally): Array<U>
    fun <T> of(vararg items: T): Array<T>
    @nativeInvoke
    operator fun invoke(arrayLength: Number = definedExternally): Array<Any>
    @nativeInvoke
    operator fun <T> invoke(arrayLength: Number): Array<T>
    @nativeInvoke
    operator fun <T> invoke(vararg items: T): Array<T>
    fun isArray(arg: Any): Boolean
    var prototype: Array<Any>
}

external interface PromiseLike<T> {
    fun then(onfulfilled: ((value: T) -> dynamic)? = definedExternally, onrejected: ((reason: Any) -> dynamic)? = definedExternally): PromiseLike<dynamic /* TResult1 | TResult2 */>
}

external interface ArrayLike<T> {
    var length: Number
    @nativeGetter
    operator fun get(n: Number): T?
    @nativeSetter
    operator fun set(n: Number, value: T)
}

typealias ReturnType<T> = Any

typealias InstanceType<T> = Any

external interface ArrayBufferTypes {
    var SharedArrayBuffer: SharedArrayBuffer
    var ArrayBuffer: ArrayBuffer
}

typealias ArrayBufferLike = Any

external interface ArrayBufferConstructor {
    var prototype: ArrayBuffer
    fun isView(arg: Any): Boolean
}

external interface DataViewConstructor

external interface Int8ArrayConstructor {
    fun from(arrayLike: Iterable<Number>, mapfn: (v: Number, k: Number) -> Number = definedExternally, thisArg: Any = definedExternally): Int8Array
    var prototype: Int8Array
    var BYTES_PER_ELEMENT: Number
    fun of(vararg items: Number): Int8Array
    fun from(arrayLike: ArrayLike<Number>): Int8Array
    fun <T> from(arrayLike: ArrayLike<T>, mapfn: (v: T, k: Number) -> Number, thisArg: Any = definedExternally): Int8Array
}

external interface Uint8ArrayConstructor {
    fun from(arrayLike: Iterable<Number>, mapfn: (v: Number, k: Number) -> Number = definedExternally, thisArg: Any = definedExternally): Uint8Array
    var prototype: Uint8Array
    var BYTES_PER_ELEMENT: Number
    fun of(vararg items: Number): Uint8Array
    fun from(arrayLike: ArrayLike<Number>): Uint8Array
    fun <T> from(arrayLike: ArrayLike<T>, mapfn: (v: T, k: Number) -> Number, thisArg: Any = definedExternally): Uint8Array
}

external interface Uint8ClampedArrayConstructor {
    fun from(arrayLike: Iterable<Number>, mapfn: (v: Number, k: Number) -> Number = definedExternally, thisArg: Any = definedExternally): Uint8ClampedArray
    var prototype: Uint8ClampedArray
    var BYTES_PER_ELEMENT: Number
    fun of(vararg items: Number): Uint8ClampedArray
    fun from(arrayLike: ArrayLike<Number>): Uint8ClampedArray
    fun <T> from(arrayLike: ArrayLike<T>, mapfn: (v: T, k: Number) -> Number, thisArg: Any = definedExternally): Uint8ClampedArray
}

external interface Int16ArrayConstructor {
    fun from(arrayLike: Iterable<Number>, mapfn: (v: Number, k: Number) -> Number = definedExternally, thisArg: Any = definedExternally): Int16Array
    var prototype: Int16Array
    var BYTES_PER_ELEMENT: Number
    fun of(vararg items: Number): Int16Array
    fun from(arrayLike: ArrayLike<Number>): Int16Array
    fun <T> from(arrayLike: ArrayLike<T>, mapfn: (v: T, k: Number) -> Number, thisArg: Any = definedExternally): Int16Array
}

external interface Uint16ArrayConstructor {
    fun from(arrayLike: Iterable<Number>, mapfn: (v: Number, k: Number) -> Number = definedExternally, thisArg: Any = definedExternally): Uint16Array
    var prototype: Uint16Array
    var BYTES_PER_ELEMENT: Number
    fun of(vararg items: Number): Uint16Array
    fun from(arrayLike: ArrayLike<Number>): Uint16Array
    fun <T> from(arrayLike: ArrayLike<T>, mapfn: (v: T, k: Number) -> Number, thisArg: Any = definedExternally): Uint16Array
}

external interface Int32ArrayConstructor {
    fun from(arrayLike: Iterable<Number>, mapfn: (v: Number, k: Number) -> Number = definedExternally, thisArg: Any = definedExternally): Int32Array
    var prototype: Int32Array
    var BYTES_PER_ELEMENT: Number
    fun of(vararg items: Number): Int32Array
    fun from(arrayLike: ArrayLike<Number>): Int32Array
    fun <T> from(arrayLike: ArrayLike<T>, mapfn: (v: T, k: Number) -> Number, thisArg: Any = definedExternally): Int32Array
}

external interface Uint32ArrayConstructor {
    fun from(arrayLike: Iterable<Number>, mapfn: (v: Number, k: Number) -> Number = definedExternally, thisArg: Any = definedExternally): Uint32Array
    var prototype: Uint32Array
    var BYTES_PER_ELEMENT: Number
    fun of(vararg items: Number): Uint32Array
    fun from(arrayLike: ArrayLike<Number>): Uint32Array
    fun <T> from(arrayLike: ArrayLike<T>, mapfn: (v: T, k: Number) -> Number, thisArg: Any = definedExternally): Uint32Array
}

external interface Float32ArrayConstructor {
    fun from(arrayLike: Iterable<Number>, mapfn: (v: Number, k: Number) -> Number = definedExternally, thisArg: Any = definedExternally): Float32Array
    var prototype: Float32Array
    var BYTES_PER_ELEMENT: Number
    fun of(vararg items: Number): Float32Array
    fun from(arrayLike: ArrayLike<Number>): Float32Array
    fun <T> from(arrayLike: ArrayLike<T>, mapfn: (v: T, k: Number) -> Number, thisArg: Any = definedExternally): Float32Array
}

external interface Float64ArrayConstructor {
    fun from(arrayLike: Iterable<Number>, mapfn: (v: Number, k: Number) -> Number = definedExternally, thisArg: Any = definedExternally): Float64Array
    var prototype: Float64Array
    var BYTES_PER_ELEMENT: Number
    fun of(vararg items: Number): Float64Array
    fun from(arrayLike: ArrayLike<Number>): Float64Array
    fun <T> from(arrayLike: ArrayLike<T>, mapfn: (v: T, k: Number) -> Number, thisArg: Any = definedExternally): Float64Array
}