plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

allprojects {
    repositories {
        jcenter()
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>().configureEach {
        kotlinOptions {
            moduleKind = "commonjs"
        }
    }
}

kotlin {
    js {
        browser {
            testTask {
                useMocha()
                testLogging {
                    showStandardStreams = true
                }
            }
        }
        binaries.executable()
    }
}

configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    nodeVersion = "12.18.3"
}

val String.v: String get() = rootProject.extra["$this.version"] as String

dependencies {
    implementation(project(":lib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${"kotlinx-coroutines".v}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${"kotlinx-serialization".v}")
    implementation("org.jetbrains:kotlin-extensions:${"kotlin-wrappers".v}-kotlin-${"kotlin".v}")

    testImplementation(kotlin("test-js"))
}
