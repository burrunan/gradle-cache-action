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

@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS",
    "DEPRECATION")

package actions.httpclient

import NodeJS.ReadableStream
import http.IncomingMessage
import http.RequestOptions
import url.Url
import kotlin.js.Promise

external interface IHeaders {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
}

external interface IHttpClient {
    fun options(requestUrl: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    fun get(requestUrl: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    fun del(requestUrl: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    fun post(requestUrl: String, data: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    fun patch(requestUrl: String, data: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    fun put(requestUrl: String, data: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    fun sendStream(verb: String, requestUrl: String, stream: ReadableStream, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    fun request(verb: String, requestUrl: String, data: String, headers: IHeaders): Promise<IHttpClientResponse>
    fun request(verb: String, requestUrl: String, data: ReadableStream, headers: IHeaders): Promise<IHttpClientResponse>
    fun requestRaw(info: IRequestInfo, data: String): Promise<IHttpClientResponse>
    fun requestRaw(info: IRequestInfo, data: ReadableStream): Promise<IHttpClientResponse>
    fun requestRawWithCallback(info: IRequestInfo, data: String, onResult: (err: Any, res: IHttpClientResponse) -> Unit)
    fun requestRawWithCallback(info: IRequestInfo, data: ReadableStream, onResult: (err: Any, res: IHttpClientResponse) -> Unit)
}

external interface IRequestHandler {
    fun prepareRequest(options: RequestOptions)
    fun canHandleAuthentication(response: IHttpClientResponse): Boolean
    fun handleAuthentication(httpClient: IHttpClient, requestInfo: IRequestInfo, objs: Any): Promise<IHttpClientResponse>
}

external interface IHttpClientResponse {
    var message: IncomingMessage
    fun readBody(): Promise<String>
}

external interface IRequestInfo {
    var options: RequestOptions
    var parsedUrl: Url
    var httpModule: Any
}

external interface IRequestOptions {
    var headers: IHeaders?
    var socketTimeout: Number?
    var ignoreSslError: Boolean?
    var allowRedirects: Boolean?
    var allowRedirectDowngrade: Boolean?
    var maxRedirects: Number?
    var maxSockets: Number?
    var keepAlive: Boolean?
    var deserializeDates: Boolean?
    var allowRetries: Boolean?
    var maxRetries: Number?
}

external interface ITypedResponse<T> {
    var statusCode: Number
    var result: T?
    var headers: Any
}
