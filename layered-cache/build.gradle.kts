plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

kotlin {
    js {
        nodejs {
            testTask {
                useMocha()
                testLogging {
                    showStandardStreams = true
                }
            }
        }
    }
}

val String.v: String get() = rootProject.extra["$this.version"] as String

dependencies {
    api(project(":lib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${"kotlinx-coroutines".v}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${"kotlinx-serialization".v}")
    api("org.jetbrains:kotlin-extensions:${"kotlin-wrappers".v}-kotlin-${"kotlin".v}")

    testImplementation(kotlin("test-js"))
}
