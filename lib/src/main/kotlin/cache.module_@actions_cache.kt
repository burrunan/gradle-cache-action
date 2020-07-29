@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

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

typealias ValidationError = Error

typealias ReserveCacheError = Error

@JsModule("@actions/cache")
external fun restoreCache(paths: Array<String>, primaryKey: String, restoreKeys: Array<String> = definedExternally, options: DownloadOptions = definedExternally): Promise<String?>

@JsModule("@actions/cache")
external fun saveCache(paths: Array<String>, key: String, options: UploadOptions = definedExternally): Promise<Number>
