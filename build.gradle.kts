import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.RootPackageJsonTask

plugins {
    kotlin("js") version "1.4.0-rc"
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

val distributionJs by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
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
    implementation(project(":lib"))
}
