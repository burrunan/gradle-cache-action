# Gradle Cache Action

[![CI Status](https://github.com/burrunan/gradle-cache-action/workflows/CI/badge.svg)](https://github.com/burrunan/gradle-cache-action/actions)
[![Kotlin](https://img.shields.io/badge/kotlin-1.4.31-blue.svg?logo=kotlin)](http://kotlinlang.org)

This is a GitHub Action for caching Gradle caches.
In other words, this is [@actions/cache](https://github.com/actions/cache) customized for Gradle.

Key improvements over [@actions/cache](https://github.com/actions/cache) and [gradle-command-action](https://github.com/eskatos/gradle-command-action) are:
- 🚀 Gradle remote build cache backend (pulls only the needed entries from GitHub cache)
- 🎉 Support multiple remote caches via [gradle-multi-cache](https://github.com/burrunan/gradle-multi-cache) (e.g. GitHub Actions + S3)
- 👋 Simplified configuration (action name + gradle command is enough for most case)
- 👾 Less space usage (GitHub imposes overall 5GiB limit by default, so cache space matters)
- 🔗 Link to Build Scan in build results
- 💡 Gradle build failure markers added to the diff view (e.g. `compileJava` or `compileKotlin` markers right in the commit diff)

## Usage

Add the following code to your workflow file in the `.github/workflows` directory.

Note: Like with [gradle-command-action](https://github.com/eskatos/gradle-command-action), you can
specify `gradle-version: release` to test with the current release version of Gradle, `gradle-version: nightly` for testing Gradle nightly builds,
an so on (see `gradle-version` below).

Note: For the [security reasons](https://julienrenaux.fr/2019/12/20/github-actions-security-risk/)
you might want to use Git SHA rather than branch name or tag name.
In other words, to avoid accidental tag update, you might want to use
`burrunan/gradle-cache-action@03c71a8ba93d670980695505f48f49daf43704a6` rather than `burrunan/gradle-cache-action@v1`.
Please see releases page to find out the commit ids: https://github.com/burrunan/gradle-cache-action/releases

You might use the following references are:
* `v1` -- this is a *moving* qualifier. It points to the latest release among `v1.x`
* `v1.0`, `v1.1`, ... -- those are fixed versions. They won't change over time

For the best security you might want to use `burrunan/gradle-cache-action@v1` (see the ids at https://github.com/burrunan/gradle-cache-action/releases)

```yaml
- uses: burrunan/gradle-cache-action@v1
  name: Build PROJECT_NAME
  # Extra environment variables for Gradle execution (regular GitHub Actions feature)
  # Note: env must be outside of "with"
  env:
    VARIABLE: VALUE
  with:
    # If you have multiple jobs, use distinct job-id in in case you want to split caches
    # For instance, jobs with different JDK versions can't share caches
    # RUNNER_OS is added to job-id automatically
    job-id: jdk8
    # Specifies arguments for Gradle execution
    # If arguments is missing or empty, then Gradle is not executed
    arguments: build
    # arguments can be multi-line for better readability
    # arguments: |
    #  --no-paralell
    #  build
    #  -x test
    # Gradle version to use for execution:
    #   wrapper (default), current, rc, nightly, release-nightly, or
    #   versions like 6.6 (see https://services.gradle.org/versions/all)
    gradle-version: wrapper
    # Properties are passed as -Pname=value
    properties: |
      kotlin.js.compiler=ir
      kotlin.parallel.tasks.in.project=true
```

By default, the action enables the `local` build cache, and it adds a remote build cache
that stores the data in GitHub Actions cache.
However, you might want to enable the [Gradle Build Cache](https://docs.gradle.org/current/userguide/build_cache.html)
for your local builds to make them faster, or even add a remote cache instance, so your local
builds can reuse artifacts that are build on CI.

This is how you can enable local build cache (don't forget to add `--build-cache` option or
`org.gradle.caching=true` property):

```kotlin
// settings.gradle.kts
val isCiServer = System.getenv().containsKey("CI")
// Cache build artifacts, so expensive operations do not need to be re-computed
buildCache {
   local {
       isEnabled = !isCiServer
   }
}
```

## Sample integrations

Here's how you can integrate build cache to existing projects:

* Apache Calcite: https://github.com/apache/calcite/pull/2114
* Apache JMeter: https://github.com/apache/jmeter/pull/611
* pgjdbc: https://github.com/pgjdbc/pgjdbc/pull/1862
* junit-pioneer: https://github.com/junit-pioneer/junit-pioneer/pull/325
* opentelemetry-java-instrumentation: https://github.com/open-telemetry/opentelemetry-java-instrumentation/pull/1054

## Configuration

The default configuration should suit most of the cases, however, there are extra knobs:

```yaml
- uses: burrunan/gradle-cache-action@v1
  name: Cache .gradle
  # Extra environment variables for Gradle execution (regular GitHub Actions feature)
  env:
    VARIABLE: VALUE
  with:
    # If you have multiple jobs, use distinct job-id in in case you want to split caches
    # For instance, jobs with different JDK versions can't share caches
    # RUNNER_OS is added to job-id automatically
    job-id: jdk8

    # Overrides $HOME
    # home-directory: /home/user

    # Disable caching of $HOME/.gradle/caches/*.*/generated-gradle-jars
    save-generated-gradle-jars: false

    # Disable remote cache that proxies requests to GitHub Actions cache
    remote-build-cache-proxy-enabled: false

    # Set the cache key for Gradle version (e.g. in case multiple jobs use different versions)
    # By default the value is `wrapper`, so the version is determined from the gradle-wrapper.properties   
    # Note: this argument specifies the version for Gradle execution (if `arguments` is present)
    # Supported values:
    #   wrapper (default), current, rc, nightly, release-nightly, or
    #   versions like 6.6 (see https://services.gradle.org/versions/all)
    gradle-version: 6.5.1-custom

    # Makes all non-main branch builds to use read-only caching
    read-only: ${{ github.ref != 'refs/heads/main' }}

    # Arguments for Gradle execution
    arguments: build jacocoReport

    # Properties are passed as -Pname=value
    properties: |
      kotlin.js.compiler=ir
      kotlin.parallel.tasks.in.project=true

    # Relative path under $GITHUB_WORKSPACE where Git repository is placed
    build-root-directory: sub/directory

    # Activates only the caches that are relevant for executing gradle command.
    # This is helpful when build job executes multiple gradle commands sequentially.
    # Then the caching is implemented in the very first one, and the subsequent should be marked
    # with execution-only-caches: true
    execution-only-caches: true

    # Disable caching of ~/.gradle/caches/build-cache-*
    save-local-build-cache: false

    # Disable caching of ~/.gradle/caches/modules-*
    save-gradle-dependencies-cache: false

    # Extra files to take into account for ~/.gradle/caches dependencies
    gradle-dependencies-cache-key: |
      gradle/dependencies.kt
      buildSrc/**/Version.kt

    # Disable caching of ~/.m2/repository/
    save-maven-dependencies-cache: false

    # Ignore some of the paths when caching Maven Local repository
    maven-local-ignore-paths: |
      org/example/
      com/example/

    # Enable concurrent cache save and restore
    # Default is concurrent=false for better log readability
    concurrent: true

    # Disable publishing Gradle Build Scan URL to job report
    gradle-build-scan-report: false

    # Disable warning about missing distributionSha256Sum property in gradle-wrapper.properties
    gradle-distribution-sha-256-sum-warning: false
```

## How does dependency caching work?

The current GitHub Action's cache (both [actions/cache](https://github.com/actions/cache) action and
[@actions/cache](https://github.com/actions/toolkit/tree/main/packages/cache) npm package) is immutable.

The cache can't be updated, so it does not work very good for caches like "Gradle dependencies" or "Maven local repository".

`gradle-cache-action` creates a layered cache, and it uses a small "index" cache to identify the required layers.
If only a small fraction of files changes, then the action reuses the existing caches, and it adds a layer on top of it.
That enables to save cache space (GitHub has a default limit of 5 GiB), and it reduces upload time as only
the cache receives only the updated files.

## How does GitHub Actions-based Gradle remote build cache work?

`gradle-cache-action` launches a small proxy server that listens for Gradle requests and
then it redirects the requests to the `@actions/cache` API.

That makes Gradle believe it is talking to a regular remote cache, and the cache receives
only the relevant updates.
The increased granularity enables GitHub to evict entries better (it removes unused entries
automatically).

The action configures the URL to the cache proxy via the `~/.gradle/init.gradle` script, and
[Gradle picks it up automatically](https://docs.gradle.org/current/userguide/init_scripts.html)

Note: Saving GitHub Actions caches might take noticeable time (e.g. 100 ms), so the cache uploads
in the background. In other words, build scan would show virtually zero response times for
cache save operations.

If your build already has a remote cache declared (e.g. you are using your own cache),
then `gradle-cache-action` would configure **both** remote caches.
It would read from the GitHub cache first, and it would save data to both caches.

The multi-cache feature can be disabled via `multi-cache-enabled: false`.

## How to enable build scans?

1. Read and agree to the terms of service: https://gradle.com/terms-of-service
1. Add `--scan` to `arguments:`, and add the following to `settings.gradle.kts`

```kotlin
plugins {
    `gradle-enterprise`
}

val isCiServer = System.getenv().containsKey("CI")

if (isCiServer) {
    gradleEnterprise {
        buildScan {
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
            tag("CI")
        }
    }
}
```

## Why another action instead of gradle-command-action?

`gradle-command-action` was started as a Kotlin/JS experiment for making a customized
[@actions/cache](https://github.com/actions/cache) that would make Gradle builds faster.

Then it turned out there's a proxy remote cache requests to the `@actions/cache` API can be used when the caching
action executes Gradle, so the `gradle-cache-action` got a Gradle execution feature.

Of course, the same could have been made in [gradle-command-action](https://github.com/eskatos/gradle-command-action),
however:
- The author was not familiar with TypeScript ecosystem (stdlib, typical libraries, testing libraries, etc.)
- Caching logic is collections-heavy, and Kotlin stdlib shines here.

  For instance, in Kotlin `list + list` adds lists, and `array.associateWith { valueFor(it) }` converts arrays to maps.
  This is easy to write without consulting StackOverflow, the code is readable, and it does not require
  you [to fight with the compiler](https://blog.johnnyreilly.com/2016/06/create-es2015-map-from-array-in-typescript.html).

- A single language helps when building connected components.
  `gradle-cache-action` integrates with [gradle-multi-cache](https://github.com/burrunan/gradle-multi-cache) and
  [gradle-s3-build-cache](https://github.com/burrunan/gradle-s3-build-cache), and they all are Kotlin-based. 

## Can I use the caching part of the action only?

Yes, you can. If you omit `arguments:`, then the action runs in `cache-only` mode.
It won't launch Gradle.

## Can I call multiple different Gradle builds in the same job?

This might be complicated, see https://github.com/burrunan/gradle-cache-action/issues/15.

Currently, the workaround is to configure `execution-only-caches: true` for all but one
`gradle-cache-action` executions.
Then one of the actions would do the cache save and restore, and the rest would use their own
caches only.

## Contributing

Contributions are always welcome! If you'd like to contribute (and we hope you do) please open a pull request.

## License

Apache 2.0

## Author

Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
