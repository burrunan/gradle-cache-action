rootProject.name = "gradle-cache-action"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven ("https://dl.bintray.com/kotlin/kotlin-eap") {
            content {
                includeGroupByRegex("^org\\.jetbrains\\.kotlin\\..*")
            }
        }
    }
}