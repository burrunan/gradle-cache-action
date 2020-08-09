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
import tsstdlib.PromiseLike

external interface AsyncIterator<T, TReturn, TNext> {
    fun next(vararg args: dynamic /* JsTuple<> | JsTuple<TNext> */): Promise<dynamic /* IteratorYieldResult<T> | IteratorReturnResult<TReturn> */>
    fun `return`(value: TReturn): Promise<dynamic /* IteratorYieldResult<T> | IteratorReturnResult<TReturn> */>
    fun `return`(value: PromiseLike<TReturn>): Promise<dynamic /* IteratorYieldResult<T> | IteratorReturnResult<TReturn> */>
    fun `throw`(e: Any): Promise<dynamic /* IteratorYieldResult<T> | IteratorReturnResult<TReturn> */>
}

external interface AsyncIterator__1<T> : AsyncIterator<T, Any, Nothing?>
