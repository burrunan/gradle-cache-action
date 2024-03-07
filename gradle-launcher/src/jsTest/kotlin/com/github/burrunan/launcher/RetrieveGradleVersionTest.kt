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

import actions.core.info
import actions.tool.cache.findAllVersions
import com.github.burrunan.test.runTest
import kotlin.test.Test

class RetrieveGradleVersionTest {
    @Test
    fun retrieve() = runTest {
        for (version in GradleVersion.DYNAMIC_VERSIONS) {
            val res = version.findUrl()
            println("$version => $res")
        }
    }

    @Test
    fun listTools() = runTest {
        listTool("gradle")
        listTool("Gradle")
        listTool("mvn")
        listTool("Maven")
    }

    private fun listTool(tool: String) {
        info("All $tool versions: ${findAllVersions(tool).joinToString(", ")}")
    }
}
