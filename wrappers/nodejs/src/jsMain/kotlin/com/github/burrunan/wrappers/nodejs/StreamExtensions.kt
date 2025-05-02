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

import js.objects.unsafeJso
import node.ReadableStream
import node.WritableStream
import node.buffer.Buffer
import node.stream.Readable
import node.stream.consumers.buffer
import node.stream.consumers.json
import node.stream.finished

suspend fun <T> Readable.readJson(): T = json(this) as T

suspend fun Readable.readToBuffer(): Buffer<*> = buffer(this)

suspend fun <T : ReadableStream, D: WritableStream> T.pipeAndWait(destination: D, end : Boolean = false) {
    pipe(destination = destination, options = unsafeJso { this.end = end })
    finished(this)
}
