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
package com.github.burrunan.gradle.github

fun Long.formatBytes() = when {
    this < 5 * 1024 -> "${this} B"
    this < 5 * 1024 * 1204 -> "${(this + 512L) / (1024L)} KiB"
    this < 5L * 1024 * 1204 * 1024 -> "${(this + 512L * 1024) / (1024L * 1024)} MiB"
    else -> "${(this + 512L * 1024 * 1024) / (1024L * 1024 * 1024)} GiB"
}
