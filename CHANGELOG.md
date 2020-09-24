## 2020-09-24 🚑 support branch names with slashes

`feature/branch` is a valid branch name in Git, so `gradle-cache-action` now escapes `/` when
using branch name as a part of the cache key.

## 2020-08-20 🚑 better exclusions for *.lock files

It turns out caching action can't apply exclude if user included of the parent folders.

See https://github.com/actions/cache/issues/364#issuecomment-678118231

It should fix errors like

```
C:\windows\System32\tar.exe -z -cf cache.tgz -P -C D:/a/... --files-from manifest.txt
tar.exe: Couldn't open C:/Users/runneradmin/.gradle/caches/6.6/generated-gradle-jars/generated-gradle-jars.lock: Permission denied
tar.exe: Error exit delayed from previous errors.
```

## 2020-08-20 🙈 silence insecure protocols warning

Modern Gradle versions issue a warning when users configure `http://` build cache.
In practice, GitHub-based cache is located on localhost, so it is fine to use http.

The plugin adds the relevant configuration to silence Gradle warning.

## 2020-08-19 🚀 Unlock Gradle remote build caching

* Add HTTP caching proxy that implements Gradle HTTP cache API [effb04a](https://github.com/burrunan/gradle-cache-action/commit/effb04a)

This enables Gradle to use GitHub cache API like a regular remote build cache service,
so the caching is more efficient. Gradle fetches only the objects it needs,
and it uploads only what was changed. Cache eviction is managed by GitHub.

The remote build cache feature activates when you use `with: arguments: build ...`
to launch the build. In other words, you need to launch Gradle via `gradle-cache-action`
rather that regular `run:` or `gradle-command-action`.

Here's how you can integrate build cache to existing projects:

* Apache Calcite: https://github.com/apache/calcite/pull/2114
* Apache JMeter: https://github.com/apache/jmeter/pull/611
* pgjdbc: https://github.com/pgjdbc/pgjdbc/pull/1862
* junit-pioneer: https://github.com/junit-pioneer/junit-pioneer/pull/325
* opentelemetry-java-instrumentation: https://github.com/open-telemetry/opentelemetry-java-instrumentation/pull/1054

### Fixes

* Reduce verbosity of "cache already exists" warning to info [8ff7dd7](https://github.com/burrunan/gradle-cache-action/commit/8ff7dd7)
* Gradle dependencies: treat *.gradle.kts as a part of the cache key [1c59269](https://github.com/burrunan/gradle-cache-action/commit/1c59269)
* Parse properties from properties tag rather than from arguments [140d8d9](https://github.com/burrunan/gradle-cache-action/commit/140d8d9)
* Add wrappers [597d5ea](https://github.com/burrunan/gradle-cache-action/commit/597d5ea)

## 2020-08-15 Support schedule and workflow_dispatch events

* Treat workflow_dispatch event (manual launch) the same as "build from the default branch) [48e88ce](https://github.com/burrunan/gradle-cache-action/commit/48e88ce)
* Use defaultbranch for schedule-based builds by default [8d3e9cc](https://github.com/burrunan/gradle-cache-action/commit/8d3e9cc)
* 🐛 Avoid build failures on schedule events: use a fixed "defaultbranch" instead of computing the branch name [6957760](https://github.com/burrunan/gradle-cache-action/commit/6957760)

### Fixes

* 🥅 Ignore unreadable files when hasing (and print warning) [01d4c8f](https://github.com/burrunan/gradle-cache-action/commit/01d4c8f)
* 🐛 avoid adding /**/ mask to Gradle dependencies key [6d3e893](https://github.com/burrunan/gradle-cache-action/commit/6d3e893)
* Add exception message to hashFiles(...) [d5f863c](https://github.com/burrunan/gradle-cache-action/commit/d5f863c)
* Disable minification for better error reporting in GitHub [29d2590](https://github.com/burrunan/gradle-cache-action/commit/29d2590)
* 🐛 Add missing await in mkdir(String) [e4a441d](https://github.com/burrunan/gradle-cache-action/commit/e4a441d)
* 🐛 Add missing JsModule declaration [54f0245](https://github.com/burrunan/gradle-cache-action/commit/54f0245)
* 🐛 Fix release publishing [b52aa2f](https://github.com/burrunan/gradle-cache-action/commit/b52aa2f)
* Split modules [365cc69](https://github.com/burrunan/gradle-cache-action/commit/365cc69)
* 🔨 Split modules [ec0c31a](https://github.com/burrunan/gradle-cache-action/commit/ec0c31a)
* ✅ Add basic tests for cache store and restore, fix invalid "always partial restore" status [594213e](https://github.com/burrunan/gradle-cache-action/commit/594213e)
* Add test for hashFilesDetailed [ce7fa0f](https://github.com/burrunan/gradle-cache-action/commit/ce7fa0f)

## 2020-07-27

✨ add gradle-dependencies-cache-key property for configuring extra dependencies (e.g. versions declared in buildSrc/Versions.kt) [cc7a294](https://github.com/burrunan/gradle-cache-action/commit/cc7a294)

## 2020-07-27 🎉 Initial version

The following caches are saved and restored:
* Gradle dependencies (~/.gradle/caches/modules-2)
* Gradle local build cache (~/.gradle/caches/build-cache-1)
* Gradle generated jars (~/.gradle/caches/*.*/generated-gradle-jars)
* Maven local repository (~/.m2/repository)
