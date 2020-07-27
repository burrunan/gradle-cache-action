@file:JsModule("@actions/cache")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package github.actions.cache

import kotlin.js.Promise

external fun restoreCache(paths: Array<String>, primaryKey: String, restoreKeys: Array<String> = definedExternally, options: DownloadOptions = definedExternally): Promise<String?>

external fun saveCache(paths: Array<String>, key: String, options: UploadOptions = definedExternally): Promise<Number>
