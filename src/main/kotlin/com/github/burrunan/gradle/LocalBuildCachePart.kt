package com.github.burrunan.gradle

import com.github.burrunan.gradle.cache.*
import com.github.burrunan.gradle.github.event.ActionsTrigger
import github.actions.cache.restoreCache
import github.actions.core.info
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

fun localBuildCache(trigger: ActionsTrigger, gradleVersion: String, treeId: String): Cache {
    val prefixChars = "0123456789abcdef"
    val defaultBranch = trigger.event.repository.default_branch
    val caches = mutableListOf<Cache>()
    val buildCacheLocation = "~/build-cache-*"

    val pk = when(trigger) {
        is ActionsTrigger.PullRequest -> "PR-${trigger.event.pull_request.number}-$treeId"
        is ActionsTrigger.BranchPush -> "${trigger.event.ref}-$treeId"
        is ActionsTrigger.Other -> throw IllegalStateException("Unknown event ${trigger.name}")
    }
    val restoreKeys = when(trigger) {
        is ActionsTrigger.PullRequest -> arrayOf(
            trigger.event.pull_request.base.ref,
            defaultBranch
        )
        is ActionsTrigger.BranchPush -> arrayOf(
            trigger.event.ref,
            defaultBranch
        )
        is ActionsTrigger.Other -> throw IllegalStateException("Unknown event ${trigger.name}")
    }
    // Branch:
    // - gradle-build-cache-$gradleVersion-$branchName-$treeId
    // - gradle-build-cache-$gradleVersion-$branchName
    // - gradle-build-cache-$gradleVersion-$defaultBranch

    // PR:
    // - gradle-build-cache-$gradleVersion-PR-$PR-$treeId
    // - gradle-build-cache-$gradleVersion-$baseRef
    // - gradle-build-cache-$gradleVersion-$defaultBranch
    for (char in prefixChars) {
        val prefix = "gradle-build-cache-$gradleVersion-$char"
        caches.add(
            DefaultCache(
                name = "local-build-cache-$char",
                primaryKey = "$prefix-$pk",
                restoreKeys = restoreKeys.map { "$prefix-$it" },
                paths = if (char != '0') {
                    listOf(
                        "$buildCacheLocation/$char*",
                        "!$buildCacheLocation/*.lock"
                    )
                } else {
                    prefixChars.map {
                        "!$buildCacheLocation/$char*"
                    } + "!$buildCacheLocation/*.lock"
                }
            )
        )
    }
    return CompositeCache("build-cache", listOf())
}
