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

package com.github.burrunan.gradle.proxy

import actions.exec.exec
import actions.glob.removeFiles
import com.github.burrunan.gradle.cache.CacheService
import com.github.burrunan.test.runTest
import com.github.burrunan.wrappers.nodejs.mkdir
import js.core.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToDynamic
import node.fs.copyFile
import node.fs.writeFile
import node.process.process
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class CacheProxyTest {
    // Emulates Azure Cache Backend for @actions/cache
    val cacheService = CacheService()

    // Implements Gradle HTTP Build Cache via @actions/cache
    val cacheProxy = CacheProxy()

    @Test
    fun abc() = runTest {
        val z = mapOf("a" to 4, "b" to 6)
        println("json: " + JSON.stringify(Json.encodeToDynamic(z)))
    }

    @Test
    fun cacheProxyWorks() = runTest {
        val dir = "remote_cache_test"
        mkdir(dir)
        val root = process.cwd() + "/../../../.."
        console.log(root)
        cacheService {
            cacheProxy {
                val outputFile = "build/out.txt"
                removeFiles(listOf("$dir/$outputFile"))
                copyFile("$root/gradlew", dir + "/gradlew")
                mkdir("$dir/gradle")
                mkdir("$dir/gradle/wrapper")
                copyFile("$root/gradle/wrapper/gradle-wrapper.jar", "$dir/gradle/wrapper/gradle-wrapper.jar")
                copyFile("$root/gradle/wrapper/gradle-wrapper.properties", "$dir/gradle/wrapper/gradle-wrapper.properties")
                writeFile(
                    "$dir/settings.gradle",
                    """
                        rootProject.name = 'sample'
                        boolean gradle6Plus = org.gradle.util.GradleVersion.current() >= org.gradle.util.GradleVersion.version('6.0')
                        buildCache {
                            local {
                                // Only remote cache should be used
                                enabled = false
                            }
                            remote(HttpBuildCache) {
                                url = '${process.env["GHA_CACHE_URL"]}'
                                push = true
                                if (gradle6Plus) {
                                    allowInsecureProtocol = true
                                }
                            }
                        }
                    """.trimIndent(),
                )
                writeFile(
                    "$dir/build.gradle",
                    """
                        tasks.create('props', WriteProperties) {
                          outputFile = file("$outputFile")
                          property("hello", "world")
                        }
                        tasks.create('props2', WriteProperties) {
                          outputFile = file("${outputFile}2")
                          property("hello", "world2")
                        }
                    """.trimIndent(),
                )
                writeFile(
                    "$dir/gradle.properties",
                    """
                    org.gradle.caching=true
                    #org.gradle.caching.debug=true
                    org.gradle.configuration-cache=true
                    """.trimIndent(),
                )

                val out = exec("./gradlew", "props", "-i", "--configuration-cache", "--build-cache", captureOutput = true) {
                    cwd = dir
                    silent = true
                    ignoreReturnCode = true
                }
                if (out.exitCode != 0) {
                    fail("Unable to execute :props task: STDOUT: ${out.stdout}, STDERR: ${out.stderr}")
                }
                assertTrue(
                    "1 actionable task: 1 executed" in out.stdout,
                    "Output should include <<1 actionable task: 1 executed>>, got: " + out.stdout,
                )

                removeFiles(listOf("$dir/$outputFile"))
                val out2 = exec("./gradlew", "props", "-i", "--configuration-cache","--build-cache", captureOutput = true) {
                    cwd = dir
                    silent = true
                    ignoreReturnCode = true
                }
                if (out.exitCode != 0) {
                    fail("Unable to execute :props task: STDOUT: ${out.stdout}, STDERR: ${out.stderr}")
                }
                assertTrue("1 actionable task: 1 from cache" in out2.stdout, out2.stdout)
            }
        }
    }
}
