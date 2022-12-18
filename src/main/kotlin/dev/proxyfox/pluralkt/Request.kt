package dev.proxyfox.pluralkt

import dev.proxyfox.pluralkt.types.*
import io.ktor.util.reflect.*

class Request<T>(val endpoint: String, val type: RequestType, val token: String?, val data: T?, val typeInfo: TypeInfo, val onComplete: Response<T>.() -> Unit) {
    fun createUrl(baseUrl: String) = baseUrl+endpoint
}

inline fun <reified T> get(endpoint: String, token: String? = null, noinline onComplete: Response<T>.() -> Unit) = Request(endpoint, RequestType.GET, token, null, typeInfo<T>(), onComplete)
inline fun <reified T> post(endpoint: String, data: T? = null, token: String, noinline onComplete: Response<T>.() -> Unit) = Request(endpoint, RequestType.POST, token, data, typeInfo<T>(), onComplete)
inline fun <reified T> patch(endpoint: String, data: T? = null, token: String, noinline onComplete: Response<T>.() -> Unit) = Request(endpoint, RequestType.PATCH, token, data, typeInfo<T>(), onComplete)
inline fun <reified T> delete(endpoint: String, token: String, noinline onComplete: Response<T>.() -> Unit) = Request(endpoint, RequestType.DELETE, token, null, typeInfo<T>(), onComplete)

enum class RequestType {
    GET,
    POST,
    PATCH,
    DELETE
}
