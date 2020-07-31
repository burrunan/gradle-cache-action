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

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

inline fun <T : Any> jsObject(builder: T.() -> Unit = {}): T =
    (js("({})") as T).apply(builder)

suspend inline fun suspendWithCallback(crossinline block: ((Error?) -> Unit) -> Unit) =
    suspendCoroutine<Nothing?> { cont ->
        block.invoke { error ->
            when (error) {
                null -> cont.resume(null)
                else -> cont.resumeWithException(error)
            }
        }
    }

suspend inline fun <T : Any> suspendWithCallback(crossinline block: ((Error?, result: T) -> Unit) -> Unit) =
    suspendCoroutine<T> { cont ->
        block.invoke { error, result ->
            when (error) {
                null -> cont.resume(result)
                else -> cont.resumeWithException(error)
            }
        }
    }
