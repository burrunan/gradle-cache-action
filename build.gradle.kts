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

//tasks.withType<RootPackageJsonTask>().configureEach {
//    doLast {
//        // Ensure all the nested packages use the updated versions of node-fetch
//        rootPackageJson.writeText(
//            rootPackageJson.readText()
//                .replaceFirst(
//                    "{", """
//                    {
//                      "resolutions": {
//                        "**/node-fetch": "3.0.0-beta.7"
//                      },
//                """.trimIndent()
//                )
//        )
//    }
//}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${"kotlinx-coroutines".v}")
//    implementation("org.jetbrains:kotlin-extensions:")
    implementation(project(":lib"))
}

//val browserProductionWebpack by tasks.existing(org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack::class)

//(artifacts) {
//    distributionJs(browserProductionWebpack.map { it.destinationDirectory!! })
//}
