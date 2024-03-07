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

import actions.cache.RestoreType
import actions.core.ext.group
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class CompositeCache(
    override val name: String,
    private val caches: List<Cache>,
    private val concurrent: Boolean,
) : Cache {
    override suspend fun save() {
        if (!concurrent) {
            for (cache in caches) {
                group("Save ${cache.name}") {
                    cache.save()
                }
            }
            return
        }

        supervisorScope {
            for (cache in caches) {
                launch {
                    cache.save()
                }
            }
        }
    }

    override suspend fun restore(): RestoreType {
        if (!concurrent) {
            for (cache in caches) {
                group("Restore ${cache.name}") {
                    cache.restore()
                }
            }
            return RestoreType.Unknown
        }

        supervisorScope {
            for (cache in caches) {
                launch {
                    cache.restore()
                }
            }
        }
        return RestoreType.Unknown
    }
}
