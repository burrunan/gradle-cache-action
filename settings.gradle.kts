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

pluginManagement {
    plugins {
        kotlin("multiplatform") version "1.9.22"
        kotlin("plugin.serialization") version "1.9.22"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "gradle-cache-action"

include(
    "cache-service-mock",
    "cache-proxy",
    "gradle-launcher",
    "hashing",
    "layered-cache",
    "cache-action-entrypoint",
    "test-library",
    "wrappers:js",
    "wrappers:nodejs",
    "wrappers:actions-toolkit",
    "wrappers:actions-cache",
    "wrappers:java-properties",
    "wrappers:octokit-request-error",
    "wrappers:octokit-types",
    "wrappers:octokit-webhooks"
)
