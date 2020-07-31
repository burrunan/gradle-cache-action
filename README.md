# Gradle Cache Action

`release` branch is used to release the action only.
Please check [main branch](https://github.com/burrunan/gradle-cache-action) for more information.

## Installation

1. You might want to enable [Gradle Build Cache](https://docs.gradle.org/current/userguide/build_cache.html)
For instance, add `--build-cache` option whe 

1. Add the following to `.github/workflows/...`

```yaml
- uses: burrunan/gradle-cache-action@release
  name: Cache .gradle
  with:
    # If you have multiple jobs, use distinct job-id in in case you want to split caches
    # For instance, jobs with different JDK versions can't share caches
    # RUNNER_OS is added to job-id automatically
    job-id: jdk8
```

For more details please check [main branch](https://github.com/burrunan/gradle-cache-action) for more information.

## License

Apache 2.0

## Author

Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
