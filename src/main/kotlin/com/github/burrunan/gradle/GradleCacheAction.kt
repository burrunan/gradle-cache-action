package com.github.burrunan.gradle

import com.github.burrunan.gradle.cache.Cache
import com.github.burrunan.gradle.github.stateVariable
import com.github.burrunan.gradle.github.suspendingStateVariable
import github.actions.core.info
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class GradleCacheAction(refName: String) {
    enum class Stage {
        PRE, POST
    }

    private val stage: Stage

    private val caches = mutableListOf<Cache>()
    private lateinit var generatedJarsCache: GradleGeneratedJarsCache
    private lateinit var buildCache: LocalBuildCache

    init {
        info("ref: $refName")
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
            hashFiles("gradle/wrapper/gradle-wrapper.properties")
        }

        caches.add(
            GradleGeneratedJarsCache(gradleVersion.get())
        )

        when (stage) {
            Stage.PRE -> pre()
            Stage.POST -> post()
        }
    }

    suspend fun pre() {
        info("running pre")
        supervisorScope {
            for (cache in caches) {
                launch {
                    cache.restore()
                }
            }
        }
    }

    suspend fun post() {
        info("running post")
        supervisorScope {
            for (cache in caches) {
                launch {
                    cache.save()
                }
            }
        }
    }
}
