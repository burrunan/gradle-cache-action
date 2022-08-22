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

import kotlinx.js.jso
import node.ReadableStream
import node.WritableStream
import node.buffer.Buffer
import node.buffer.BufferEncoding
import node.stream.finished

suspend fun <T> ReadableStream.readJson(): T = JSON.parse(readToBuffer().toString(BufferEncoding.utf8))

suspend fun ReadableStream.readToBuffer(): Buffer {
    val data = mutableListOf<Buffer>()
    on("data") { chunk: Any ->
        data += chunk as Buffer
    }
    finished(this)
    return Buffer.concat(data.toTypedArray())
}

suspend fun <T : ReadableStream, D: WritableStream> T.pipeAndWait(destination: D, end : Boolean = false) {
    if (end) {
        pipe(destination = destination, options = jso { this.end = true })
    } else {
        pipe(destination = destination)
    }
    finished(this)
}
