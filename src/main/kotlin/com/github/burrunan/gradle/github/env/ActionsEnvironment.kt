package com.github.burrunan.gradle.github.env

import process
import kotlin.reflect.KProperty

/**
 * See https://docs.github.com/en/actions/configuring-and-managing-workflows/using-environment-variables#default-environment-variables
 */
object ActionsEnvironment {
    val HOME by Environment
    val GITHUB_WORKFLOW by Environment
    val GITHUB_RUN_ID by Environment
    val GITHUB_RUN_NUMBER by Environment
    val GITHUB_ACTION by Environment
    val GITHUB_ACTOR by Environment
    val GITHUB_REPOSITORY by Environment
    val GITHUB_EVENT_NAME by Environment
    val GITHUB_EVENT_PATH by Environment
    val GITHUB_WORKSPACE by Environment
    val GITHUB_SHA by Environment
    val GITHUB_REF by Environment
    val GITHUB_HEAD_REF by Environment
    val GITHUB_BASE_REF by Environment
    val GITHUB_SERVER_URL by Environment
    val GITHUB_API_URL by Environment
    val GITHUB_GRAPHQL_URL by Environment
}

private object Environment {
    operator fun getValue(environment: Any, property: KProperty<*>): String =
        process.env[property.name] ?: throw IllegalStateException("${property.name} is not found in process.env")
}
