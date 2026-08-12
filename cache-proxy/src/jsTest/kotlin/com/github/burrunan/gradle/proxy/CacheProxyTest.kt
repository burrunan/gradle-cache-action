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

import actions.core.ActionFailedException
import actions.exec.ExecOptions
import actions.exec.exec
import actions.glob.removeFiles
import com.github.burrunan.gradle.cache.CacheService
import com.github.burrunan.test.runTest
import com.github.burrunan.wrappers.nodejs.mkdir
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToDynamic
import node.buffer.BufferEncoding
import node.buffer.utf8
import node.fs.copyFile
import node.fs.writeFile
import node.process.process
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
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

    /** Returns the port of a started proxy, so a test can pin it or occupy it. */
    private fun CacheProxy.boundPort(): Int {
        val url = assertNotNull(cacheUrl, "the proxy must publish its URL once started")
        return url.removePrefix("http://localhost:").removeSuffix("/").toInt()
    }

    /**
     * Returns a port that was free at the moment of the call.
     *
     * Hard-coding one would make the suite depend on a port nobody else uses, and the README example
     * is the first value a developer running this locally would have taken.
     */
    private suspend fun freePort(): Int {
        val probe = CacheProxy()
        probe.start()
        try {
            return probe.boundPort()
        } finally {
            probe.stop()
        }
    }

    /**
     * Runs [block] against a pinned port, retrying when something else claims that port first.
     *
     * Releasing a port does not reserve it, so probing for a free one and then binding it is a
     * check-then-act race. Only a failed bind retries: an assertion inside [block] propagates on the
     * first attempt, so a proxy that ignored its port argument still fails here rather than looping.
     */
    private suspend fun withPinnedPort(attempts: Int = 5, block: suspend (Int) -> Unit) {
        repeat(attempts - 1) {
            try {
                return block(freePort())
            } catch (e: ActionFailedException) {
                // Someone took the port between the probe and the bind, so probe again
            }
        }
        block(freePort())
    }

    @Test
    fun pinnedPortReachesTheGeneratedInitScript() = runTest {
        withPinnedPort { port ->
            val pinnedProxy = CacheProxy(port = port)
            pinnedProxy {
                assertEquals("http://localhost:$port/", pinnedProxy.cacheUrl)
                assertContains(pinnedProxy.getMultiCacheConfiguration(), "url = 'http://localhost:$port/'")
            }
        }
    }

    /**
     * Fails when two runs pinning the same port generate different init scripts.
     *
     * Gradle treats init script content as a configuration input, so any part of the script that
     * varies between runs discards the configuration cache — the symptom being
     * `Calculating task graph as configuration cache cannot be reused because init script
     * 'init.gradle' has changed`. The proxy URL was that varying part, and asserting the whole
     * script rather than the URL alone means anything else that starts varying fails here too.
     */
    @Test
    fun aPinnedPortKeepsTheInitScriptIdenticalAcrossRuns() = runTest {
        withPinnedPort { port ->
            val runs = (1..2).map {
                val proxy = CacheProxy(port = port)
                proxy { proxy.getMultiCacheConfiguration() }
            }
            assertEquals(runs[0], runs[1], "init script of two runs pinning port $port")
        }
    }

    @Test
    fun ephemeralPortsDifferBetweenConcurrentProxies() = runTest {
        val first = CacheProxy()
        val second = CacheProxy()
        first {
            second {
                // Two proxies bound at the same time cannot share a port, so equal ports would mean
                // the default stopped being ephemeral
                assertNotEquals(first.boundPort(), second.boundPort(), "default ports of two live proxies")
            }
        }
    }

    @Test
    fun bindFailureNamesThePortAndTheInput() = runTest {
        val occupant = CacheProxy()
        occupant {
            val port = occupant.boundPort()
            val failure = assertFailsWith<ActionFailedException> {
                CacheProxy(port = port).start()
            }
            val message = assertNotNull(failure.message)
            assertContains(message, port.toString(), message = "the port must be named: $message")
            assertContains(
                message,
                "remote-build-cache-proxy-port",
                message = "the input that sets the port must be named: $message",
            )
            // The only part Node supplies, and the code action.yml promises the user
            assertContains(message, "EADDRINUSE", message = "the reason Node gave must survive: $message")
        }
    }

    @Test
    fun bindFailureOnAnEphemeralPortNamesNoInput() {
        val message = CacheProxy.bindFailureMessage(0, "listen EACCES: permission denied")
        assertContains(message, "listen EACCES: permission denied")
        assertFalse(
            "remote-build-cache-proxy-port" in message,
            "a bind that failed on an ephemeral port gives the user nothing to set: $message",
        )
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
                    BufferEncoding.utf8,
                )
                writeFile(
                    "$dir/build.gradle",
                    """
                        tasks.create('props', WriteProperties) {
                          destinationFile = file("$outputFile")
                          property("hello", "world")
                        }
                        tasks.create('props2', WriteProperties) {
                          destinationFile = file("${outputFile}2")
                          property("hello", "world2")
                        }
                    """.trimIndent(),
                    BufferEncoding.utf8,
                )
                writeFile(
                    "$dir/gradle.properties",
                    """
                    org.gradle.caching=true
                    #org.gradle.caching.debug=true
                    org.gradle.configuration-cache=true
                    """.trimIndent(),
                    BufferEncoding.utf8,
                )
                val out = exec("./gradlew", "props", "-i", "--build-cache", captureOutput = true) {
                    ExecOptions.copy(it,
                        cwd = dir,
                        silent = true,
                        ignoreReturnCode = true,
                    )
                }
                if (out.exitCode != 0) {
                    fail("Unable to execute :props task: STDOUT: ${out.stdout}, STDERR: ${out.stderr}")
                }
                assertContains(
                    out.stdout,
                    "1 actionable task: 1 executed",
                )

                removeFiles(listOf("$dir/$outputFile"))
                val out2 = exec("./gradlew", "props", "-i", "--build-cache", captureOutput = true) {
                    ExecOptions.copy(it,
                        cwd = dir,
                        silent = true,
                        ignoreReturnCode = true,
                    )
                }
                if (out.exitCode != 0) {
                    fail("Unable to execute :props task: STDOUT: ${out.stdout}, STDERR: ${out.stderr}")
                }
                assertContains(out2.stdout, "1 actionable task: 1 from cache")
            }
        }
    }
}
