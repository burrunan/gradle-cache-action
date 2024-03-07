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

package com.github.burrunan.launcher

sealed class GradleVersion(val name: String, unused: Int = 0) {
    companion object {
        val DYNAMIC_VERSIONS = listOf(
            Current,
            ReleaseCandidate,
            Nightly,
            ReleaseNightly,
        )
        val FIXED_VERSIONS = DYNAMIC_VERSIONS + Wrapper
    }

    abstract class Dynamic(label: String, val apiPath: String) : GradleVersion(label)
    class Official(label: String) : GradleVersion(label) {
        override fun toString() = "Official($name)"
    }

    object Current : Dynamic("current", "current") {
        override fun toString() = "Current"
    }

    object ReleaseCandidate : Dynamic("rc", "release-candidate") {
        override fun toString() = "ReleaseCandidate"
    }

    object Nightly : Dynamic("nightly", "nightly") {
        override fun toString() = "Nightly"
    }

    object ReleaseNightly : Dynamic("release-nightly", "release-nightly") {
        override fun toString() = "ReleaseNightly"
    }

    object Wrapper : GradleVersion("wrapper") {
        override fun toString() = "Wrapper"
    }
}

fun GradleVersion(version: String) =
    GradleVersion.FIXED_VERSIONS.firstOrNull { it.name == version }
        ?: GradleVersion.Official(version)
