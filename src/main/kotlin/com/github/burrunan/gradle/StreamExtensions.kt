package com.github.burrunan.gradle

import NodeJS.ReadableStream
import NodeJS.WritableStream
import stream.Duplex
import stream.internal
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T : ReadableStream, R : Any> T.use(action: suspend (T) -> R): R {
    try {
        return action(this)
    } finally {
        finished()
    }
}

suspend fun ReadableStream.finished() =
    suspendCoroutine<Nothing?> { cont ->
        internal.finished(this@finished) {
            when (it) {
                null -> cont.resume(null)
                else -> cont.resumeWithException(it)
            }
        }
    }

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
fun <T : Duplex> ReadableStream.pipe(duplex: T, end: Boolean = true): WritableStream =
    pipe(duplex as WritableStream, jsObject { this.end = end })
