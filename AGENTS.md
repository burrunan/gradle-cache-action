# gradle-cache-action

A GitHub Action written in Kotlin/JS and executed by Node. `cache-action-entrypoint` is the entry point,
`cache-proxy` serves Gradle's HTTP build cache over the `@actions/cache` API and generates the
`~/.gradle/init.gradle` that points Gradle at it, and `layered-cache` handles the dependency caches.
[README.md](README.md) describes what the action does for its users; what follows is what is easy to get
wrong while changing it.

## Build and test

```bash
./gradlew build
```

Test tasks are per platform, and `jsTest` is only an aggregate: `cache-proxy` runs under
`:cache-proxy:jsNodeTest`, `cache-action-entrypoint` under `:cache-action-entrypoint:jsBrowserTest`. Filtering
works on the platform task, not on the aggregate, which rejects the option outright:

```bash
./gradlew :cache-proxy:jsNodeTest --tests '*bindFailure*'
```

Results land in `<module>/build/test-results/jsNodeTest/` or `.../jsBrowserTest/`, never under `jsTest`. A
stale XML there reads exactly like a green run of tests that never executed, so check that the task actually
ran before trusting a report.

`kotlinStoreYarnLock` fails with `Lock file was changed` after a dependency change. Regenerate the lock file:

```bash
./gradlew kotlinUpgradeYarnLock
```

`kotlin-js-store/` is neither tracked nor ignored, so it surfaces as an untracked directory.

`dist/` is built by CI on the `release` branch. A feature branch leaves it alone.

## Node APIs

Node declarations come from the `kotlin-wrappers` BOM pinned in the root `build.gradle.kts`, and ship as
`.klib` archives with no sources. Unpacking one to recover a signature costs more than asking the compiler:
pass a deliberately wrong type and read the expected one out of the error message.

Events are typed rather than string-keyed. `server.errorEvent.addHandler { (error) -> ... }` returns the
function that deregisters the handler, and the payload is a `Tuple1`, so destructuring it needs
`import js.array.component1`.

## The generated init.gradle has to be the same on every run

Gradle fingerprints init script content as a configuration input, so anything
`CacheProxy.getMultiCacheConfiguration()` interpolates that differs between two runs of the same workflow
discards the user's configuration cache every build. Keep what the script carries derived from action inputs,
which are stable by definition. `CacheProxyTest.aPinnedPortKeepsTheInitScriptIdenticalAcrossRuns` compares
whole scripts rather than looking for one value, so a newly added varying element fails there instead of in
somebody's build.

The proxy URL is the exception the rule is worth stating for, since it carries a port the OS assigns.
`remote-build-cache-proxy-port` pins it, and stays opt-in with a default of `0` because two jobs on one
machine cannot pin the same port.

## Failures have to surface as ActionFailedException

`main()` catches `ActionFailedException` and nothing else, so any other exception escapes as an unhandled
rejection whose stack trace names neither the input at fault nor a remedy. Node APIs are where this is easy
to miss: a failed `listen` arrives as an `'error'` event rather than as a thrown exception, and an
unobserved `'error'` terminates the process outright.
