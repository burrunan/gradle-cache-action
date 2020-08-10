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

import fs2.promises.unlink
import actions.glob.create
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun removeFiles(files: List<String>) {
    if (files.isEmpty()) {
        return
    }
    val globber = create(files.joinToString("\n")).await()
    val fileNames = globber.glob().await()
    supervisorScope {
        for (file in fileNames) {
            launch {
                unlink(file).await()
            }
        }
    }
}

suspend fun mkdir(path: String) {
    if (!exists(path)) {
        fs2.promises.mkdir(path).await()
    }
}

suspend fun exists(path: String) =
    suspendCoroutine<Boolean> { cont ->
        fs.exists(path.normalizedPath) {
            cont.resume(it)
        }
    }

val String.normalizedPath: String
    get() = when {
        startsWith("~") -> os.homedir() + substring(1)
        else -> this
    }
