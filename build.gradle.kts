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
        nodeVersion = "12.18.3"
    }
}

subprojects {
    if (path != ":wrappers") {
        apply(plugin = "org.jetbrains.kotlin.js")
    }
}

allprojects {
    repositories {
        jcenter()
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
                "api"("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${"kotlinx-serialization".v}")
            }
            "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-core:${"kotlinx-coroutines".v}")
            "implementation"("org.jetbrains:kotlin-extensions:${"kotlin-wrappers".v}-kotlin-${"kotlin".v}")
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
                            }
                        }
                    }
                }
            }
        }
    }
}
