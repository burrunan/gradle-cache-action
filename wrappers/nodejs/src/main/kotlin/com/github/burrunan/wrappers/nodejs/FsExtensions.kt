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
package com.github.burrunan.wrappers.nodejs

import js.objects.jso
import node.fs.MakeDirectoryOptions
import node.fs.existsSync
import node.fs.mkdir

suspend fun mkdir(path: String) {
    if (!exists(path)) {
        mkdir(path, jso<MakeDirectoryOptions> { recursive = true })
    }
}

@Deprecated(message = "catch errors instead", level = DeprecationLevel.WARNING)
fun exists(path: String) =
    existsSync(path.normalizedPath)

val String.normalizedPath: String
    get() = when {
        startsWith("~") -> node.os.homedir() + substring(1)
        else -> this
    }
