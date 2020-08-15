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
@file:JsModule("fs")
@file:JsQualifier("promises")
package fs2.promises

import kotlin.js.Promise

@JsName("unlink")
external fun unlinkAsync(paths: String): Promise<Unit>

@JsName("readFile")
external fun readFileAsync(path: String, encoding: String = definedExternally): Promise<String>

@JsName("writeFile")
external fun writeFileAsync(path: String, data: Any, encoding: String = definedExternally): Promise<Unit>

@JsName("stat")
external fun statAsync(path: String): Promise<fs.Stats>

@JsName("rename")
external fun renameAsync(oldPath: String, newPath: String): Promise<Unit>

@JsName("mkdir")
external fun mkdirAsync(path: String): Promise<Unit>

@JsName("chmod")
external fun chmodAsync(path: String, mode: Number): Promise<Unit>

@JsName("chmod")
external fun chmodAsync(path: String, mode: String): Promise<Unit>
