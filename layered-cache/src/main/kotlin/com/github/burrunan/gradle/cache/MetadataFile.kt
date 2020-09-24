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
package com.github.burrunan.gradle.cache

import actions.core.warning
import com.github.burrunan.wrappers.nodejs.exists
import com.github.burrunan.wrappers.nodejs.normalizedPath
import fs2.promises.readFile
import fs2.promises.rename
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class MetadataFile<T>(name: String, private val serializer: KSerializer<T>, private val extension: String = ".json") {
    companion object {
        const val ROOT_FOLDER = "~/.gradle-cache-action"
        val SPECIAL_CHARS = Regex("""[!@#$%^&*:;'"{}\[\]\\]""")

        init {
            val path = ROOT_FOLDER.normalizedPath
            if (!fs.existsSync(path)) {
                try {
                    fs.mkdirSync(path)
                } catch (ignored: Throwable) {
                }
            }
        }
    }

    val cachedName = "$ROOT_FOLDER/$name$extension"
    private var uniqueName = cachedName.normalizedPath

    fun prepare(key: String) {
        uniqueName = "${cachedName.normalizedPath}.${key.replace('/', '-')}"
    }

    suspend fun restore(key: String) {
        val path = cachedName.normalizedPath
        if (exists(path)) {
            prepare(key)
            rename(path, uniqueName)
        } else {
            warning("$cachedName: $path does not exist")
        }
    }

    suspend fun decode(warnOnMissing: Boolean = true): T? {
        if (!exists(uniqueName)) {
            if (warnOnMissing) {
                warning("$cachedName: $uniqueName does not exist")
            }
            return null
        }
        return try {
            Json.decodeFromString(
                serializer,
                readFile(uniqueName)
            )
        } catch (e: SerializationException) {
            warning("$cachedName: error deserializing $uniqueName with ${serializer.descriptor.serialName}, message: $e")
            return null
        }
    }

    suspend fun encode(value: T) {
        fs2.promises.writeFile(
            cachedName.normalizedPath,
            Json.encodeToString(serializer, value),
            "utf8",
        )
    }
}
