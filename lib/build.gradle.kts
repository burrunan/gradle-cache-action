import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.js")
}

kotlin {
    js {
        nodejs()
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.suppressWarnings = true
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.4")
    api(npm("@actions/core", "1.2.4"))
    api(npm("@actions/github", "4.0.0"))
    api(npm("@actions/glob", "0.1.0"))
    api(npm("@actions/cache", "1.0.1"))
}
