plugins {
    kotlin("js") apply false
}

allprojects {
    repositories {
        jcenter()
    }
    plugins.withId("org.jetbrains.kotlin.js") {
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>().configureEach {
            kotlinOptions {
                moduleKind = "commonjs"
            }
        }
    }
}

plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
        nodeVersion = "12.18.3"
    }
}
