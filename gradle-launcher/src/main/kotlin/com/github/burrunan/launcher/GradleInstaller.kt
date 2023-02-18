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

import actions.core.ActionFailedException
import actions.core.info
import actions.core.warning
import actions.http.client.HttpClient
import actions.http.client.HttpCodes
import actions.io.rmRF
import actions.tool.cache.cacheDir
import actions.tool.cache.downloadTool
import actions.tool.cache.extractZip
import com.github.burrunan.hashing.hashFiles
import com.github.burrunan.wrappers.nodejs.exists
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import js.core.recordOf
import node.buffer.BufferEncoding
import node.fs.chmod
import node.fs.readFile
import node.path.path
import node.process.Platform

suspend fun install(distribution: GradleDistribution): String {
    val cachedTool = actions.tool.cache.find("gradle", distribution.version)
    val gradleDir = if (cachedTool.isNotEmpty()) {
        info("Detected Gradle ${distribution.version} at $cachedTool")
        cachedTool
    } else {
        val gradleZip = downloadTool(distribution.distributionUrl)
        distribution.distributionSha256Sum?.let { expectedSha256 ->
            val hash = hashFiles(gradleZip, algorithm = "sha256", includeFileName = false).hash
            if (hash != expectedSha256) {
                throw ActionFailedException(
                    "Checksum mismatch for Gradle ${distribution.version} (${distribution.distributionUrl}). " +
                        "Expected: $expectedSha256, actual: $hash",
                )
            }
        }
        val extractedGradleDir = extractZip(gradleZip)
        cacheDir(path.join(extractedGradleDir, "gradle-${distribution.version}"), "gradle", distribution.version).also {
            GlobalScope.launch {
                // Remove temporary files
                rmRF(gradleZip)
                rmRF(extractedGradleDir)
            }
        }
    }
    return path.join(gradleDir, "bin", if (node.os.platform() == Platform.win32) "gradle.bat" else "gradle").also {
        if (node.os.platform() != Platform.win32) {
            chmod(it, "755".toInt(8))
        }
    }
}

private val HTTP_AGENT = recordOf<String, Any>("User-Agent" to "burrunan/gradle-cache-action")

suspend fun GradleVersion.Official.findUrl(): GradleDistribution {
    val url = "https://services.gradle.org/versions/all"
    val response =
        HttpClient().getJson<Array<GradleVersionResponse>>(url, HTTP_AGENT).await()
    if (response.statusCode.unsafeCast<HttpCodes>() != HttpCodes.OK) {
        throw ActionFailedException("Unable to lookup $url Gradle version: ${response.statusCode}, ${JSON.stringify(response.result)}")
    }
    return response.result?.firstOrNull { it.version == name }?.resolveChecksum()
        ?: throw ActionFailedException("Unable to find Gradle version $name")
}

suspend fun GradleVersion.Dynamic.findUrl(): GradleDistribution {
    val url = "https://services.gradle.org/versions/$apiPath"
    val response = HttpClient().getJson<dynamic>(url, HTTP_AGENT).await()
    if (response.statusCode.unsafeCast<HttpCodes>() != HttpCodes.OK) {
        throw ActionFailedException("Unable to lookup $url Gradle version: ${response.statusCode}, ${JSON.stringify(response.result)}")
    }
    if (response.result?.version != null) {
        return response.result.unsafeCast<GradleVersionResponse>().resolveChecksum()
    }
    if (this is GradleVersion.ReleaseCandidate) {
        return GradleVersion.Current.findUrl()
    }
    throw ActionFailedException("Empty result from $url: ${JSON.stringify(response.result)}")
}

suspend fun GradleVersionResponse.resolveChecksum() =
    GradleDistribution(
        version = version,
        distributionUrl = downloadUrl,
        distributionSha256Sum = HttpClient().get(checksumUrl, HTTP_AGENT).await().readBody().await().trim(),
    )

suspend fun findVersionFromWrapper(projectPath: String, enableDistributionSha256SumWarning: Boolean): GradleDistribution {
    val gradleWrapperProperties = path.join(projectPath, "gradle", "wrapper", "gradle-wrapper.properties")
    if (!exists(gradleWrapperProperties)) {
        warning("Gradle wrapper configuration is not found at ${path.resolve(gradleWrapperProperties)}.\nWill use the current release Gradle version")
        return GradleVersion.Current.findUrl()
    }
    val propString = readFile(gradleWrapperProperties, BufferEncoding.utf8)
    val props = javaproperties.parseString(propString).run {
        getKeys().associateWith { getFirst(it)!! }
    }

    val distributionUrl = props.getValue("distributionUrl")
    val distributionSha256Sum = props["distributionSha256Sum"]

    val version = distributionUrl.substringAfterLast("/")
        .substringAfter("gradle-")
        .removeSuffix("-all.zip")
        .removeSuffix("-bin.zip")
        .removeSuffix(".zip")

    if (enableDistributionSha256SumWarning && distributionSha256Sum == null) {
        warning(
            "distributionSha256Sum is not set in $gradleWrapperProperties.\n" +
                "Please consider adding the checksum, " +
                "see https://docs.gradle.org/current/userguide/gradle_wrapper.html#configuring_checksum_verification",
        )
    }

    return if (distributionUrl.removePrefix("https").removePrefix("http")
            .startsWith("://services.gradle.org/")
    ) {
        // Official release, use shorter version
        //   https://services.gradle.org/distributions-snapshots/gradle-6.7-20200730220045+0000-all.zip
        //   https://services.gradle.org/distributions/gradle-6.6-rc-4-all.zip
        //   https://services.gradle.org/distributions/gradle-6.5.1-all.zip
        if (distributionUrl.endsWith("-bin.zip") && distributionSha256Sum != null) {
            GradleDistribution(version, distributionUrl, distributionSha256Sum)
        } else {
            // Resolve checksum from the official site
            // This would switch to -bin distribution which is smaller
            GradleVersion.Official(version).findUrl()
        }
    } else {
        GradleDistribution(version, distributionUrl, distributionSha256Sum)
    }
}
