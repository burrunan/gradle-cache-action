# Gradle Cache Action

[![CI Status](https://github.com/burrunan/gradle-cache-action/workflows/CI/badge.svg)](https://github.com/burrunan/gradle-cache-action/actions)
![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/burrunan/gradle-cache-action?label=release)

This is a GitHub Action for caching Gradle caches.
In other words, this is [@actions/cache](https://github.com/actions/cache) customized for Gradle.

Key improvements over [@actions/cache](https://github.com/actions/cache) and the official [gradle/actions/setup-gradle](https://github.com/gradle/actions/blob/main/docs/setup-gradle.md) are:
- 🆓 100% MIT-licensed. Since `setup-gradle@v6`, the default `enhanced` caching uses the proprietary `gradle-actions-caching` library. `gradle-cache-action` stays open source end to end.
- 🚀 Real Gradle remote build cache backend (pulls only the entries the build needs). `setup-gradle` snapshots the whole Gradle User Home, including `caches/build-cache-1`, as a single blob.
- 🎉 Multiple remote caches via [gradle-multi-cache](https://github.com/burrunan/gradle-multi-cache), so GitHub Actions cache can run alongside S3 or any other backend.
- 👾 Layered cache cuts upload time and storage, which matters because GitHub imposes a 10 GB cache limit per repository by default.
- 💡 Gradle build failure markers in the pull request diff view (e.g. `compileJava` or `compileKotlin` markers right next to the offending line).

## Version notes

- `v3` is the current line and runs on `node24`. Use it on GitHub-hosted runners and on self-hosted runners that ship Node 24.
- `v2` runs on `node20` and remains available for runners that do not yet ship Node 24.
- `v1` runs on `node16`, which GitHub has deprecated. Upgrade by changing the version reference.

## Usage

Add the following code to your workflow file in the `.github/workflows` directory.

Note: You can specify `gradle-version: release` to test with the current release version of Gradle,
`gradle-version: nightly` for Gradle nightly builds, and so on (see `gradle-version` below).

Note: For the [security reasons](https://julienrenaux.fr/2019/12/20/github-actions-security-risk/)
you might want to use Git SHA rather than branch name or tag name.
In other words, to avoid accidental tag update, you might want to use
`burrunan/gradle-cache-action@03c71a8ba93d670980695505f48f49daf43704a6` rather than `burrunan/gradle-cache-action@v1`.
Please see releases page to find out the commit ids: https://github.com/burrunan/gradle-cache-action/releases

You might use the following references are:
* `v1`, `v2` -- this is a *moving* qualifier. It points to the latest release among `v1.x`
* `v1.0`, `v1.1`, ... -- those are fixed versions. They won't change over time

For the best security you might want to use `burrunan/gradle-cache-action@v3` (see the ids at https://github.com/burrunan/gradle-cache-action/releases)

```yaml
- uses: burrunan/gradle-cache-action@v3
  name: Build PROJECT_NAME
  # Extra environment variables for Gradle execution (regular GitHub Actions feature)
  # Note: env must be outside of "with"
  env:
    VARIABLE: VALUE
  with:
    # If you have multiple jobs, use a distinct job-id when you want to split caches
    # For instance, jobs with different JDK versions can't share caches
    # RUNNER_OS is added to job-id automatically
    job-id: jdk8
    # Specifies arguments for Gradle execution
    # If arguments is missing or empty, then Gradle is not executed
    arguments: build
    # arguments can be multi-line for better readability
    # arguments: |
    #  --no-parallel
    #  build
    #  -x test
    # Gradle version to use for execution:
    #   wrapper (default), current, rc, nightly, release-nightly, or
    #   versions like 9.0 (see https://services.gradle.org/versions/all)
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

Reference pull requests that wire `gradle-cache-action` into existing projects:

* Apache Calcite: https://github.com/apache/calcite/pull/2114
* Apache JMeter: https://github.com/apache/jmeter/pull/611
* pgjdbc: https://github.com/pgjdbc/pgjdbc/pull/1862

## Configuration

The default configuration should suit most of the cases, however, there are extra knobs:

```yaml
- uses: burrunan/gradle-cache-action@v3
  name: Cache .gradle
  # Extra environment variables for Gradle execution (regular GitHub Actions feature)
  env:
    VARIABLE: VALUE
  with:
    # If you have multiple jobs, use distinct job-id in case you want to split caches
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
    #   versions like 9.0 (see https://services.gradle.org/versions/all)
    gradle-version: 9.0-custom

    # Makes all non-main branch builds to use read-only caching
    read-only: ${{ github.ref != 'refs/heads/main' }}

    # Uncomment to keep Gradle Daemon after the build
    # daemon: true

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
That saves cache space (GitHub has a default limit of 10 GB per repository), and it reduces upload time because the
cache only receives the updated files.

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

1. Read and agree to the terms of use: https://gradle.com/help/legal-terms-of-use
1. Add `--scan` to `arguments:`, and add the following to `settings.gradle.kts`:

```kotlin
// settings.gradle.kts
plugins {
    id("com.gradle.develocity") version "3.18.2"
}

val isCiServer = System.getenv().containsKey("CI")

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"
        if (isCiServer) {
            tag("CI")
        }
        publishing.onlyIf { isCiServer }
    }
}
```

Note: the `com.gradle.develocity` plugin replaces the older `com.gradle.enterprise` plugin starting from Develocity Gradle plugin 3.17.

## Why use this instead of gradle/actions/setup-gradle?

`gradle/actions/setup-gradle` is the official action and works well for many projects.
Pick `gradle-cache-action` when one of these matters:

- **100% MIT-licensed.** Since `setup-gradle@v6`, the default `enhanced` caching is provided by the proprietary
  `gradle-actions-caching` library. Switching to its `basic` provider drops most of the advanced caching features.
  `gradle-cache-action` is open source end to end.
- **Real Gradle remote build cache backend.** The action runs a small proxy that speaks the Gradle build cache protocol
  and forwards entries to the GitHub Actions cache, so builds fetch only the entries they need. `setup-gradle` instead
  snapshots the whole Gradle User Home (including `caches/build-cache-1`) as a single blob.
- **Multiple remote caches.** Combine the GitHub Actions cache with S3 or any other backend via
  [gradle-multi-cache](https://github.com/burrunan/gradle-multi-cache) and
  [gradle-s3-build-cache](https://github.com/burrunan/gradle-s3-build-cache).
- **Layered cache cuts upload time and storage.** Cache entries are split into small layers that are reused across runs.
  `setup-gradle` docs explicitly note that a layered cache is not supported.
- **Build failure markers in the pull request diff view.** Compile errors from `compileJava`, `compileKotlin`, and
  similar tasks appear inline next to the offending source line.

### Historical note

`gradle-cache-action` started as a Kotlin/JS experiment to make a customized
[@actions/cache](https://github.com/actions/cache) for Gradle builds. The Gradle execution feature followed once the
proxy-based remote cache idea proved out. The implementation is Kotlin throughout, which lines up with the related
[gradle-multi-cache](https://github.com/burrunan/gradle-multi-cache) and
[gradle-s3-build-cache](https://github.com/burrunan/gradle-s3-build-cache) projects.

## Can I use the caching part of the action only?

Yes, you can. If you omit `arguments:`, then the action runs in `cache-only` mode.
It won't launch Gradle.

## Is the Gradle distribution itself cached?

The action does not cache the downloaded Gradle distribution (`~/.gradle/wrapper/dists`).
It caches the parts of the Gradle User Home that builds reuse: dependencies (`caches/modules-2`),
the local build cache (`caches/build-cache-1`), generated Gradle jars, and the Maven repository (`~/.m2/repository`).

How the distribution is handled depends on how you run Gradle:

- **You run `./gradlew` yourself** (no `arguments:`). The wrapper downloads and unpacks the distribution on every run,
  and the action does not cache it.
- **The action runs Gradle** (`arguments:` is set). Gradle is installed into the runner tool cache (`RUNNER_TOOL_CACHE`).
  Self-hosted runners keep that cache between runs, so the distribution is reused without another download.
  GitHub-hosted runners start clean each run, so it is downloaded again.

Caching the distribution rarely pays off: both a fresh download and a cache restore still unpack an archive of similar
size, so the only difference is the download source. It also competes for the 10 GB per-repository GitHub Actions cache
limit, where the distribution can crowd out more valuable entries such as `modules-2` and `build-cache-1`. If your case
is different, you can try a separate `actions/cache` step. Open an issue if it measurably helps, so we can understand
when distribution caching is worthwhile:

```yaml
- uses: actions/cache@v4
  with:
    path: ~/.gradle/wrapper/dists
    key: gradle-wrapper-${{ hashFiles('gradle/wrapper/gradle-wrapper.properties') }}
```

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
