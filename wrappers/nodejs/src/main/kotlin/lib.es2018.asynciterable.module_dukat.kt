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

import tsstdlib.PromiseLike
import kotlin.js.Promise

external interface AsyncIterator<T, TReturn, TNext> {
    fun next(vararg args: dynamic /* JsTuple<> | JsTuple<TNext> */): Promise<dynamic /* IteratorYieldResult<T> | IteratorReturnResult<TReturn> */>
    fun `return`(value: TReturn): Promise<dynamic /* IteratorYieldResult<T> | IteratorReturnResult<TReturn> */>
    fun `return`(value: PromiseLike<TReturn>): Promise<dynamic /* IteratorYieldResult<T> | IteratorReturnResult<TReturn> */>
    fun `throw`(e: Any): Promise<dynamic /* IteratorYieldResult<T> | IteratorReturnResult<TReturn> */>
}

external interface AsyncIterator__1<T> : AsyncIterator<T, Any, Nothing?>
