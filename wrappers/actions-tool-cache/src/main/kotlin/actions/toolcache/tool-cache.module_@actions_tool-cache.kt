/*
 * Copyright 2020 Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JsModule("@actions/tool-cache")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package actions.toolcache

import kotlin.js.Promise

@JsName("downloadTool")
external fun downloadToolAsync(url: String, dest: String = definedExternally, auth: String = definedExternally): Promise<String>

@JsName("extract7z")
external fun extract7zAsync(file: String, dest: String = definedExternally, _7zPath: String = definedExternally): Promise<String>

@JsName("extractTar")
external fun extractTarAsync(file: String): Promise<String>

@JsName("extractTar")
external fun extractTarAsync(file: String, dest: String = definedExternally): Promise<String>

@JsName("extractTar")
external fun extractTarAsync(file: String, dest: String, flags: String): Promise<String>

@JsName("extractTar")
external fun extractTarAsync(file: String, dest: String, flags: Array<String>): Promise<String>

@JsName("extractXar")
external fun extractXarAsync(file: String, dest: String = definedExternally): Promise<String>

@JsName("extractXar")
external fun extractXarAsync(file: String, dest: String, flags: String): Promise<String>

@JsName("extractXar")
external fun extractXarAsync(file: String, dest: String, flags: Array<String>): Promise<String>

@JsName("extractZip")
external fun extractZipAsync(file: String, dest: String = definedExternally): Promise<String>

@JsName("cacheDir")
external fun cacheDirAsync(sourceDir: String, tool: String, version: String, arch: String = definedExternally): Promise<String>

@JsName("cacheFile")
external fun cacheFileAsync(sourceFile: String, targetFile: String, tool: String, version: String, arch: String = definedExternally): Promise<String>

external fun find(toolName: String, versionSpec: String, arch: String = definedExternally): String

external fun findAllVersions(toolName: String, arch: String = definedExternally): Array<String>

@JsName("getManifestFromRepo")
external fun getManifestFromRepoAsync(owner: String, repo: String, auth: String = definedExternally, branch: String = definedExternally): Promise<Array<IToolRelease>>

@JsName("findFromManifest")
external fun findFromManifestAsync(versionSpec: String, stable: Boolean, manifest: Array<IToolRelease>, archFilter: String = definedExternally): Promise<IToolRelease?>
