/*
 * Copyright 2020 Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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
    val RUNNER_OS by Environment
}

private object Environment {
    operator fun getValue(environment: Any, property: KProperty<*>): String =
        process.env[property.name] ?: throw IllegalStateException("${property.name} is not found in process.env")
}
