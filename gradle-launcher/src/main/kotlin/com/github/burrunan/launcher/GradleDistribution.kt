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

data class GradleDistribution(
    val version: String,
    val distributionUrl: String,
    val distributionSha256Sum: String?,
)

suspend fun resolveDistribution(
    versionSpec: String,
    projectPath: String,
    distributionUrl: String? = null,
    distributionSha256Sum: String? = null,
): GradleDistribution {
    return if (distributionUrl == null) {
        when (val version = GradleVersion(versionSpec)) {
            is GradleVersion.Official -> version.findUrl()
            is GradleVersion.Dynamic -> version.findUrl()
            is GradleVersion.Wrapper -> findVersionFromWrapper(projectPath)
        }
    } else {
        GradleDistribution(
            version = versionSpec,
            distributionUrl = distributionUrl,
            distributionSha256Sum = distributionSha256Sum ?: "$distributionUrl.sha256",
        )
    }
}
