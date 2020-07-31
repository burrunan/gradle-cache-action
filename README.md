# Gradle Cache Action

[![CI Status](https://github.com/burrunan/gradle-cache-action/workflows/CI/badge.svg)](https://github.com/burrunan/gradle-cache-action/actions)

This ia GitHub Action for caching Gradle caches.
In other words, this is [@actions/cache](https://github.com/actions/cache) customized for Gradle.

Key improvements over `@actions/cache`
- Simplified configuration 
- Less space usage (there's overall 5GiB limit, so cache space matters)
- Native support for caching Gradle's local build cache

## Usage

Add the following to `.github/workflows/...`

```yaml
- uses: burrunan/gradle-cache-action@release
  name: Cache .gradle
  with:
    # If you have multiple jobs, use distinct job-id in in case you want to split caches
    # For instance, jobs with different JDK versions can't share caches
    # RUNNER_OS is added to job-id automatically
    job-id: jdk8
```

You might want to enable [Gradle Build Cache](https://docs.gradle.org/current/userguide/build_cache.html)
For instance, add `--build-cache` option when running Gradle.

```kotlin
// build.gradle.kts
val isCiServer = System.getenv().containsKey("CI")
// Cache build artifacts, so expensive operations do not need to be re-computed
buildCache {
   local {
       isEnabled = !isCiServer || System.getenv().containsKey("GITHUB_ACTIONS")
   }
}
```

## Configuration

The default configuration should suit for most of the cases, however, there are extra knobs.

```yaml
- uses: burrunan/gradle-cache-action@release
  name: Cache .gradle
  with:
    # If you have multiple jobs, use distinct job-id in in case you want to split caches
    # For instance, jobs with different JDK versions can't share caches
    # RUNNER_OS is added to job-id automatically
    job-id: jdk8

    # Disable caching of $HOME/.gradle/caches/*.*/generated-gradle-jars
    save-generated-gradle-jars: false

    # Set the cache key for Gradle version (e.g. in case multiple jobs use different versions)
    # By default the value is `wrapper`, so the version is determined from the gradle-wrapper.properties   
    gradle-version: 6.5.1-custom

    # Disable caching of ~/.gradle/caches/build-cache-*
    save-local-build-cache: false

    # Disable caching of ~/.gradle/caches/modules-*
    save-gradle-dependencies-cache: false

    # Disable caching of ~/.m2/repository/
    save-maven-dependencies-cache: false

    # Enable concurrent cache save and restore
    # Default is concurrent=false for better log readability
    concurrent: true
```

## How does it work?

The current GitHub Actions cache (both [actions/cache](https://github.com/actions/cache) action and
[@actions/cache](https://github.com/actions/toolkit/tree/main/packages/cache) npm package) is immutable.

The cache can't be updated, so it does not work very good for caches like "Gradle dependencies" or "Maven local repository".

`gradle-cache-action` creates a layered cache, and it uses a small "index" cache to identify the required layers.
If only a small fraction of files changes, then the action reuses the existing caches, and it adds a layer on top of it.
That enables to save cache space (GitHub has a default limit of 5GiB), and it reduces upload time as only
the cache receives only the updated files.

## Contributing

Contributions are always welcome! If you'd like to contribute (and we hope you do) please open a pull request.

## License

Apache 2.0

## Author

Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
