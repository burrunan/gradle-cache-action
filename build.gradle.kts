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

plugins {
    kotlin("js") apply false
}

val String.v: String get() = rootProject.extra["$this.version"] as String

plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
        nodeVersion = "16.16.0"
    }
}

subprojects {
    if (path != ":wrappers") {
        apply(plugin = "org.jetbrains.kotlin.js")
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
    plugins.withId("org.jetbrains.kotlin.js") {
        tasks {
            withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>().configureEach {
                kotlinOptions {
                    moduleKind = "commonjs"
                }
            }
            withType<AbstractTestTask>().configureEach {
                testLogging {
                    showStandardStreams = true
                }
            }
        }
        dependencies {
            constraints {
                "api"("org.jetbrains.kotlinx:kotlinx-serialization-core:${"kotlinx-serialization".v}")
                "api"("org.jetbrains.kotlinx:kotlinx-serialization-json:${"kotlinx-serialization".v}")
            }
            "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-core:${"kotlinx-coroutines".v}")
            "implementation"(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${"kotlin-wrappers".v}"))
            "apiDependenciesMetadata"(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${"kotlin-wrappers".v}"))
            "implementation"("org.jetbrains.kotlin-wrappers:kotlin-extensions")
            if (project.path != ":test-library") {
                "testImplementation"(project(":test-library"))
            }
        }
        configure<org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension> {
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
    }
}
