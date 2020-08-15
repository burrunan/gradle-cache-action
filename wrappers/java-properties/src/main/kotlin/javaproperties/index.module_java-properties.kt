@file:JsModule("java-properties")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package javaproperties

import kotlin.js.Json

open external class PropertiesFile(vararg args: String) {
    open var objs: Json
    open fun makeKeys(line: String)
    open fun addFile(file: String)
    open fun of(vararg args: String)
    open fun get(key: String, defaultValue: String = definedExternally): dynamic /* String? | Array<String>? */
    open fun getLast(key: String, defaultValue: String = definedExternally): String?
    open fun getFirst(key: String, defaultValue: String = definedExternally): String?
    open fun getInt(key: String, defaultIntValue: Number = definedExternally): Number?
    open fun getFloat(key: String, defaultFloatValue: Number = definedExternally): Number?
    open fun getBoolean(key: String, defaultBooleanValue: Boolean = definedExternally): Boolean
    open fun set(key: String, value: String)
    open fun interpolate(s: String): String
    open fun getKeys(): Array<String>
    open fun getMatchingKeys(matchstr: String): Array<String>
    open fun reset()
}

external fun of(args: Any): PropertiesFile
