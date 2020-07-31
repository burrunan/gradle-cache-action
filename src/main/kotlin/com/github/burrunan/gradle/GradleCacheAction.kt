package com.github.burrunan.gradle

import com.github.burrunan.gradle.cache.Cache
import com.github.burrunan.gradle.github.event.ActionsTrigger
import com.github.burrunan.gradle.github.event.currentTrigger
import com.github.burrunan.gradle.github.stateVariable
import com.github.burrunan.gradle.github.suspendingStateVariable
import github.actions.core.debug
import github.actions.core.info
import github.actions.exec.exec
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.suspendCoroutine

class GradleCacheAction(val trigger: ActionsTrigger, val params: Parameters) {
    enum class Stage {
        PRE, POST
    }

    private val stage: Stage

    private val caches = mutableListOf<Cache>()

    init {
        val preRaw = stateVariable(Stage.PRE.name)
        if (preRaw.get().isBlank()) {
            stage = Stage.PRE
            preRaw.set("Y")
        } else {
            stage = Stage.POST
        }
    }

    suspend fun run() {
        val gradleVersion = suspendingStateVariable("gradleVersion") {
            hashFiles("gradle/wrapper/gradle-wrapper.properties").hash
        }

        caches.add(gradleGeneratedJarsCache(gradleVersion.get()))

        val treeId = when(trigger) {
            is ActionsTrigger.BranchPush -> trigger.event.head_commit.tree_id
            else -> getTreeId(trigger.event.after)
        }

        if (params.debug) {
            debug("Using tree id of $treeId")
        }

        caches.add(localBuildCache(trigger, gradleVersion.get(), treeId))

        when (stage) {
            Stage.PRE -> pre()
            Stage.POST -> post()
        }
    }

    private suspend fun getTreeId(commitId: String): String {
        val treeId = exec("git", "--show", "--quiet", "--format=%T", commitId).stdout
        if (params.debug) {
            debug("Commit $commitId points to tree $treeId")
        }
        return treeId
    }

    suspend fun pre() = supervisorScope {
        for (cache in caches) {
            launch {
                cache.restore()
            }
        }
    }

    suspend fun post() = supervisorScope {
        for (cache in caches) {
            launch {
                cache.save()
            }
        }
    }
}
