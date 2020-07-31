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
        browser()
        binaries.executable()
    }
}

val String.v: String get() = rootProject.extra["$this.version"] as String

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${"kotlinx-coroutines".v}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${"kotlinx-serialization".v}")
    implementation(project(":lib"))
}
