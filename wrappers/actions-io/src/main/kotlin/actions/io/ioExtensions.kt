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

package actions.io

import kotlinext.js.jsObject
import kotlinx.coroutines.await

suspend fun cp(source: String, dest: String, recursive: Boolean = false, force: Boolean = false) =
    cpAsync(source, dest, jsObject {
        this.recursive = recursive
        this.force = force
    }).await()

suspend fun mv(source: String, dest: String, force: Boolean = false) =
    mvAsync(source, dest, jsObject {
        this.force = force
    }).await()

suspend fun rmRF(inputPath: String) = rmRFAsync(inputPath).await()

suspend fun mkdirP(fsPath: String) = mkdirPAsync(fsPath).await()

suspend fun which(tool: String, check: Boolean = false) = whichAsync(tool, check).await()
