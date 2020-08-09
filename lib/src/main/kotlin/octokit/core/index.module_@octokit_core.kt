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

external interface `T$4` {
    var plugins: Array<Any>
}

external interface `T$5` {
    var debug: (message: String, additionalInfo: Any?) -> Any
    var info: (message: String, additionalInfo: Any?) -> Any
    var warn: (message: String, additionalInfo: Any?) -> Any
    var error: (message: String, additionalInfo: Any?) -> Any
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
}

external open class Octokit(options: OctokitOptions = definedExternally) {
    open var request: Any
    open var graphql: Any
    open var log: `T$5`
    open var hook: HookCollection
    open var auth: (args: Any) -> Promise<Any>
    @nativeGetter
    open operator fun get(key: String): Any?
    @nativeSetter
    open operator fun set(key: String, value: Any)

    companion object {
        var VERSION: String
        fun <S : Constructor<Any>> defaults(defaults: OctokitOptions): Any /* Any & S */
        fun <S : Constructor<Any>> defaults(defaults: Function<*>): Any /* Any & S */
        var plugins: Array<OctokitPlugin>
        fun <S : Constructor<Any>, T : Array<OctokitPlugin>> plugin(vararg newPlugins: T): `T$4` /* `T$4` & S & Constructor<UnionToIntersection<ReturnTypeOf<T>>> */
    }
}
