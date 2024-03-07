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
 */

package com.github.burrunan.gradle.cache

import actions.core.ActionsEnvironment
import com.github.burrunan.gradle.GradleCacheAction
import octokit.ActionsTrigger

val ActionsTrigger.cacheKey: String
    get() = when (this) {
        is ActionsTrigger.PullRequest -> "PR${event.pull_request.number}"
        is ActionsTrigger.BranchPush -> when (val ref = event.ref.removePrefix("refs/heads/")) {
            event.repository.default_branch.removePrefix("refs/heads/") ->
                GradleCacheAction.DEFAULT_BRANCH_VAR
            else -> ref
        }
        is ActionsTrigger.Schedule, is ActionsTrigger.WorkflowDispatch ->
            GradleCacheAction.DEFAULT_BRANCH_VAR
        is ActionsTrigger.Other -> "$name-${ActionsEnvironment.GITHUB_WORKFLOW}-${ActionsEnvironment.GITHUB_SHA}"
    }
