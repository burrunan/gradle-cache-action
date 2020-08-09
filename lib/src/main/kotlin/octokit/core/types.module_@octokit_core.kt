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

@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package octokit.core

import kotlin.js.*
import kotlin.js.Json
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

external interface OctokitOptions {
    var authStrategy: Any?
        get() = definedExternally
        set(value) = definedExternally
    var auth: Any?
        get() = definedExternally
        set(value) = definedExternally
    var request: RequestRequestOptions?
        get() = definedExternally
        set(value) = definedExternally
    var timeZone: String?
        get() = definedExternally
        set(value) = definedExternally
    @nativeGetter
    operator fun get(option: String): Any?
    @nativeSetter
    operator fun set(option: String, value: Any)
}

typealias Constructor<T> = Any

typealias ReturnTypeOf<T> = Any

typealias UnionToIntersection<Union> = Any

typealias AnyFunction = (args: Any) -> Any

typealias OctokitPlugin = (octokit: Octokit, options: OctokitOptions) -> dynamic
