plugins {
    kotlin("js")
}

kotlin {
    js {
        browser()
        binaries.executable()
    }
}

dependencies {
    implementation(project(":layered-cache"))
}
