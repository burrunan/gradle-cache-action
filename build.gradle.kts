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

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    kotlin("multiplatform") apply false
}

plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec> {
        version = "22.0.0"
    }
}

subprojects {
    if (path != ":wrappers") {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
    }
}

allprojects {
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        configure<KotlinMultiplatformExtension> {
            js {
                useCommonJs()
            }
        }
        tasks {
            withType<AbstractTestTask>().configureEach {
                testLogging {
                    showStandardStreams = true
                }
            }
        }
        configure<KotlinMultiplatformExtension> {
            js {
                if (project.name.endsWith("-entrypoint")) {
                    browser {
                        testTask {
                            useMocha {
                                timeout = "10000"
                            }
                        }
                    }
                    binaries.executable()
                } else {
                    nodejs {
                        testTask {
                            useMocha {
                                timeout = "10000"
                                environment("RUNNER_TOOL_CACHE", layout.buildDirectory.dir("tool-cache").get().asFile.toString())
                            }
                        }
                    }
                }
            }
        }
        dependencies {
            "commonMainApi"(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))
            "commonMainApi"(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:1.8.1"))
            "jsMainImplementation"(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:2025.5.8"))
            if (project.path != ":test-library") {
                "jsTestImplementation"(rootProject.projects.testLibrary)
            }
        }
    }
}
