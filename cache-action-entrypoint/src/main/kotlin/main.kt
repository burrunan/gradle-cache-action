import com.github.burrunan.gradle.GradleCacheAction
import com.github.burrunan.gradle.Parameters
import com.github.burrunan.gradle.github.env.ActionsEnvironment
import com.github.burrunan.gradle.github.event.currentTrigger

suspend fun main() {
    if (process.env["GITHUB_ACTIONS"].isNullOrBlank()) {
        // Ignore if called outside of GitHub Actions (e.g. tests)
        return
    }
    val params = Parameters(
        jobId = ActionsEnvironment.RUNNER_OS + "-" + getInput("job-id"),
        path = getInput("path").trimEnd('/', '\\').ifBlank { "." },
        debug = getInput("debug").toBoolean(),
        generatedGradleJars = getInput("save-generated-gradle-jars").ifBlank { "true" }.toBoolean(),
        localBuildCache = getInput("save-local-build-cache").ifBlank { "true" }.toBoolean(),
        gradleDependenciesCache = getInput("save-gradle-dependencies-cache").ifBlank { "true" }.toBoolean(),
        gradleDependenciesCacheKey = getInput("gradle-dependencies-cache-key"),
        mavenDependenciesCache = getInput("save-maven-dependencies-cache").ifBlank { "true" }.toBoolean(),
        concurrent = getInput("concurrent").ifBlank { "false" }.toBoolean(),
    )

    if (!params.generatedGradleJars && !params.localBuildCache &&
        !params.gradleDependenciesCache && !params.mavenDependenciesCache
    ) {
        info("All the caches are disabled, skipping the action")
        return
    }

    GradleCacheAction(currentTrigger(), params).run()
}
