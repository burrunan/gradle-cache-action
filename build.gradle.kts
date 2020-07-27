plugins {
    kotlin("js") version "1.4.0-rc"
}

repositories {
    maven ("https://dl.bintray.com/kotlin/kotlin-eap") {
        content {
            includeGroupByRegex("^org\\.jetbrains\\.kotlin\\..*")
        }
    }
    mavenCentral()
    jcenter()
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
    implementation(kotlin("stdlib-js"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${"kotlinx-coroutines".v}")
    implementation(npm("@actions/core", "1.2.4"))
    implementation(npm("@actions/github", "4.0.0"))
    implementation(npm("@actions/cache", "1.0.1"))
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile>().configureEach {
    kotlinOptions {
        moduleKind = "commonjs"
    }
}

//val browserProductionWebpack by tasks.existing(org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack::class)

//(artifacts) {
//    distributionJs(browserProductionWebpack.map { it.destinationDirectory!! })
//}
