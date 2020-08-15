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

@file:JsModule("@actions/http-client")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package actions.httpclient

import NodeJS.ReadableStream
import http.Agent
import http.IncomingMessage
import kotlin.js.Promise

external enum class HttpCodes {
    OK /* = 200 */,
    MultipleChoices /* = 300 */,
    MovedPermanently /* = 301 */,
    ResourceMoved /* = 302 */,
    SeeOther /* = 303 */,
    NotModified /* = 304 */,
    UseProxy /* = 305 */,
    SwitchProxy /* = 306 */,
    TemporaryRedirect /* = 307 */,
    PermanentRedirect /* = 308 */,
    BadRequest /* = 400 */,
    Unauthorized /* = 401 */,
    PaymentRequired /* = 402 */,
    Forbidden /* = 403 */,
    NotFound /* = 404 */,
    MethodNotAllowed /* = 405 */,
    NotAcceptable /* = 406 */,
    ProxyAuthenticationRequired /* = 407 */,
    RequestTimeout /* = 408 */,
    Conflict /* = 409 */,
    Gone /* = 410 */,
    TooManyRequests /* = 429 */,
    InternalServerError /* = 500 */,
    NotImplemented /* = 501 */,
    BadGateway /* = 502 */,
    ServiceUnavailable /* = 503 */,
    GatewayTimeout /* = 504 */
}

external enum class Headers {
    Accept /* = "accept" */,
    ContentType /* = "content-type" */
}

external enum class MediaTypes {
    ApplicationJson /* = "application/json" */
}

external fun getProxyUrl(serverUrl: String): String

external open class HttpClientResponse(message: IncomingMessage) : IHttpClientResponse {
    override var message: IncomingMessage
    override fun readBody(): Promise<String>
}

external fun isHttps(requestUrl: String): Boolean

external open class HttpClient(userAgent: String = definedExternally, handlers: Array<IRequestHandler> = definedExternally, requestOptions: IRequestOptions = definedExternally) {
    open var userAgent: String?
    open var handlers: Array<IRequestHandler>
    open var requestOptions: IRequestOptions
    open var _ignoreSslError: Any
    open var _socketTimeout: Any
    open var _allowRedirects: Any
    open var _allowRedirectDowngrade: Any
    open var _maxRedirects: Any
    open var _allowRetries: Any
    open var _maxRetries: Any
    open var _agent: Any
    open var _proxyAgent: Any
    open var _keepAlive: Any
    open var _disposed: Any
    open fun options(requestUrl: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    open fun get(requestUrl: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    open fun del(requestUrl: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    open fun post(requestUrl: String, data: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    open fun patch(requestUrl: String, data: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    open fun put(requestUrl: String, data: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    open fun head(requestUrl: String, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    open fun sendStream(verb: String, requestUrl: String, stream: ReadableStream, additionalHeaders: IHeaders = definedExternally): Promise<IHttpClientResponse>
    open fun <T> getJson(requestUrl: String, additionalHeaders: IHeaders = definedExternally): Promise<ITypedResponse<T>>
    open fun <T> postJson(requestUrl: String, obj: Any, additionalHeaders: IHeaders = definedExternally): Promise<ITypedResponse<T>>
    open fun <T> putJson(requestUrl: String, obj: Any, additionalHeaders: IHeaders = definedExternally): Promise<ITypedResponse<T>>
    open fun <T> patchJson(requestUrl: String, obj: Any, additionalHeaders: IHeaders = definedExternally): Promise<ITypedResponse<T>>
    open fun request(verb: String, requestUrl: String, data: String, headers: IHeaders): Promise<IHttpClientResponse>
    open fun request(verb: String, requestUrl: String, data: ReadableStream, headers: IHeaders): Promise<IHttpClientResponse>
    open fun dispose()
    open fun requestRaw(info: IRequestInfo, data: String): Promise<IHttpClientResponse>
    open fun requestRaw(info: IRequestInfo, data: ReadableStream): Promise<IHttpClientResponse>
    open fun requestRawWithCallback(info: IRequestInfo, data: String, onResult: (err: Any, res: IHttpClientResponse) -> Unit)
    open fun requestRawWithCallback(info: IRequestInfo, data: ReadableStream, onResult: (err: Any, res: IHttpClientResponse) -> Unit)
    open fun getAgent(serverUrl: String): Agent
    open var _prepareRequest: Any
    open var _mergeHeaders: Any
    open var _getExistingOrDefaultHeader: Any
    open var _getAgent: Any
    open var _performExponentialBackoff: Any
    open var _processResponse: Any

    companion object {
        var dateTimeDeserializer: Any
    }
}
