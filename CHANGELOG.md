## 2024-07-25: v2, v1.21 🚀 Move to node20

* Bump `node16` to `node20`. This resolves "node16 is deprecated" warning.

* Bump Gradle to 8.8
* Bump kotlinx-coroutines to 1.8.1
* Bump kotlin-serialization to 1.7.1
* Bump wrapper-validation-action to v3
* Bump setup-java, checkout to v4

## 2023-02-18: v1.21 🚑 bump dependencies

* Add wrapper-validation-action
* Bump checkout and setup-java to v3

## 2023-02-18: v1.20 🚑 Avoid crash on missing layer-..json file

* fix: avoid failure when index restore misses layer-..json file
* Migrate to kotlin-wrappers:kotlin-actions-toolkit for better Kotlin wrappers https://github.com/burrunan/gradle-cache-action/pull/65
* Bump Gradle to 8.0.1
* Bump Kotlin to 1.8.10

## 2023-02-03: v1.19 🚑 Support nested version catalogs

* Also consider nested version catalogs in default dependency paths: https://github.com/burrunan/gradle-cache-action/issues/63

Thanks to [Vampire](https://github.com/Vampire) for the contribution.

## 2023-02-03: v1.18 🚑 fix crash when git log returns a string with a newline

* Trim the resulting SHA to prevent failures like in https://github.com/burrunan/gradle-cache-action/issues/63

## 2023-02-03: v1.17 🚀 better types for list arguments in github-workflows-kt

* Mark list arguments better: https://github.com/burrunan/gradle-cache-action/pull/61
* Add gradle/libs.versions.toml to the default dependency paths: https://github.com/burrunan/gradle-cache-action/pull/62

Thanks to [Vampire](https://github.com/Vampire) for the contribution.

## 2023-01-23: v1.16 🚀 added types for github-workflows-kt

See https://github.com/burrunan/gradle-cache-action/issues/58

Thanks to [Vampire](https://github.com/Vampire) for the contribution.

## 2022-11-27: v1.15 ⬆️ bump dependencies
Includes all the fixes from 1.13 and 1.14.

## 2022-10-29: v1.14 ⬆️ bump dependencies
*Unreleased*: the code was not compatible with `kotlin-wrappers/node`, so it did not work. Use 1.15 instead.

* bump @actions/core: 1.9.1 -> 1.10.0 (fix set-state warning)
* bump @actions/cache: 3.0.4 -> 3.0.6

## 2022-08-24: v1.13 ⬆️ bump dependencies
*Unreleased*: the code was not compatible with `kotlin-wrappers/node`, so it did not work.  Use 1.15 instead.

* bump @actions/core: 1.9.0 -> 1.9.1
* bump @actions/cache: 3.0.0 -> 3.0.4
* Move from kotlinx-node to kotlin-wrappers/node
* Print stacktrace on cache proxy server failure
* Bump Gradle to 7.5.1

## 2022-07-15: v1.12 ⬆️ bump dependencies

* Kotlin 1.4.31 legacy -> 1.7.10 IR
* @actions/cache: 1.0.1 -> 3.0.0
* @actions/core: 1.2.4 -> 1.9.0
* @actions/exec 1.0.4 -> 1.1.1
* @actions/glob 0.1.0 -> 0.3.0
* @actions/http-client 1.0.8 -> 2.0.1
* @actions/io 1.0.2 -> 1.1.2
* @actions/tool-cache 1.6.0 -> 2.0.1
* @octokit/request-error 2.0.2 -> 3.0.0
* @octokit/types 5.4.0 -> 6.39.0
* @octokit/webhooks 7.9.3 -> 10.0.8
* nodejs: 12.18.3 -> 16.16.0

Thanks to https://github.com/infomiho for https://github.com/burrunan/gradle-cache-action/pull/49

## 2022-07-06: v1.11 🚑 add home-directory property to override $HOME location

When Docker executes under root user, it will use `/root` as home directory,
so cache location would use locations like `/root/.gradle`.

The new `home-directory: /path/to/proper/user/home` property can be used to
override the location of `$HOME`.

See https://github.com/burrunan/gradle-cache-action/issues/41

## 2021-03-09: v1.10 🚀 optimize local build cache with push=false when read-only

Local build cache won't help much in the read-only mode since workers are stateless anyway.
Disabling the cache reduces the time it takes to pack cache entries.

## 2021-03-09: v1.9 🚀 optimize remote build cache with push=false when read-only

Previously `read-only` was implemented as "skip saving the caches",
however it makes sense to configure `push=false` so Gradle skips cache entry preparation as well.

## 2021-03-09: v1.8 🚑 support Gradle 5

Support Gradle 5 (and possibly even earlier versions).
Previously the plugin added `init.gradle` script to activate remote build cache aggregator.
However, `beforeSettings` is Gradle 6.0+ only, so in previous Gradle versions
`gradle-cache-action` skips `com.github.burrunan.multi-cache` plugin.

The impact is old Gradle versions would not be able to use both GitHub cache and
custom remote build cache at the same time (only project-defined remote build cache would be used).

## 2021-03-08: v1.7 🚀 support read-only cache operation

The following configuration would make all non-main branch builds to use read-only caching:
`read-only: ${{ github.ref != 'refs/heads/main' }}`.
It would save GitHub space usage for PR builds, however, they would still use
caches from the main branch.

Bump Gradle: 6.5.1 -> 6.8.3
Bump Kotlin: 1.4.0-rc -> 1.4.31
Bump kotlinx-serialization: 1.0-M1-1.4.0-rc -> 1.1.0
Bump kotlinx-serialization: 1.0-M1-1.4.0-rc -> 1.1.0
Bump kotlinx-coroutines: 1.3.8-1.4.0-rc -> 1.4.3
Bump kotlin-wrappers: 1.0.1-pre.110 -> 1.0.1-pre.148

## 2020-12-09: v1.6 🚀 added option to silence warnings

Add `gradle-distribution-sha-256-sum-warning` option to silence warning in case checksum is missing.
Add `gradle-build-scan-report` to skip publishing Gradle Build Scan URL to the job report.

Thanks to [Przemysław Jakubczyk](https://github.com/pjakubczyk) for the contributions.

## 2020-09-24: v1.5 🚑 support branch names with slashes

`feature/branch` is a valid branch name in Git, so `gradle-cache-action` now escapes `/` when
using branch name as a part of the cache key.

## 2020-08-20: v1.4 🚑 better exclusions for *.lock files

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
