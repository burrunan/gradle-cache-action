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
 *
 */
package com.github.burrunan.gradle

import NodeJS.ReadableStream
import NodeJS.WritableStream
import kotlinext.js.jsObject
import stream.Duplex
import stream.internal

suspend fun <T : ReadableStream, R : Any> T.use(action: suspend (T) -> R): R {
    try {
        return action(this)
    } finally {
        finished()
    }
}

suspend fun ReadableStream.finished() =
    suspendWithCallback {
        internal.finished(this@finished, it)
    }

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
fun <T : Duplex> ReadableStream.pipe(duplex: T, end: Boolean = true): WritableStream =
    pipe(duplex as WritableStream, jsObject { this.end = end })
