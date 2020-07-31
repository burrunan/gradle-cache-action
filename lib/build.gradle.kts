plugins {
    kotlin("js")
}

kotlin {
    js {
        nodejs()
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>().configureEach {
    kotlinOptions.suppressWarnings = true
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.4")
    api(npm("@actions/core", "1.2.4"))
    api(npm("@actions/github", "4.0.0"))
    api(npm("@actions/glob", "0.1.0"))
    api(npm("@actions/cache", "1.0.1"))
    api(npm("@actions/exec", "1.0.4"))
}
