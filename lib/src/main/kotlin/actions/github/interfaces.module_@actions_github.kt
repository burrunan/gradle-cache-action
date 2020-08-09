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

package actions.github

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

external interface `T$10` {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
    var login: String
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PayloadRepository {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
    var full_name: String?
        get() = definedExternally
        set(value) = definedExternally
    var name: String
    var owner: `T$10`
    var html_url: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$11` {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
    var number: Number
    var html_url: String?
        get() = definedExternally
        set(value) = definedExternally
    var body: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$12` {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
    var type: String
}

external interface `T$13` {
    var id: Number
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
}

external interface WebhookPayload {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
    var repository: PayloadRepository?
        get() = definedExternally
        set(value) = definedExternally
    var issue: `T$11`?
        get() = definedExternally
        set(value) = definedExternally
    var pull_request: `T$11`?
        get() = definedExternally
        set(value) = definedExternally
    var sender: `T$12`?
        get() = definedExternally
        set(value) = definedExternally
    var action: String?
        get() = definedExternally
        set(value) = definedExternally
    var installation: `T$13`?
        get() = definedExternally
        set(value) = definedExternally
    var comment: `T$13`?
        get() = definedExternally
        set(value) = definedExternally
}
