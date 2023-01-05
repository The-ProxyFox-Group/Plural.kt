package dev.proxyfox.pluralkt

import dev.proxyfox.pluralkt.types.*
import io.ktor.util.reflect.*

class Request<I, O>(val endpoint: String, val type: RequestType, val token: String?, val data: I?, val outputTypeInfo: TypeInfo, val inputTypeInfo: TypeInfo, val onComplete: Response<O>.() -> Unit) {
    fun createUrl(baseUrl: String) = baseUrl+endpoint
}

inline fun <reified O> get(endpoint: String, token: String? = null, noinline onComplete: Response<O>.() -> Unit) = Request(endpoint, RequestType.GET, token, null, typeInfo<PkType>(), typeInfo<O>(), onComplete)
inline fun <reified I, reified O> post(endpoint: String, data: I? = null, token: String, noinline onComplete: Response<O>.() -> Unit) = Request(endpoint, RequestType.POST, token, data, typeInfo<I>(), typeInfo<O>(), onComplete)
inline fun <reified I, reified O> patch(endpoint: String, data: I? = null, token: String, noinline onComplete: Response<O>.() -> Unit) = Request(endpoint, RequestType.PATCH, token, data, typeInfo<I>(), typeInfo<O>(), onComplete)
inline fun <reified O> delete(endpoint: String, token: String, noinline onComplete: Response<O>.() -> Unit) = Request(endpoint, RequestType.DELETE, token, null, typeInfo<PkType>(), typeInfo<O>(), onComplete)

enum class RequestType {
    GET,
    POST,
    PATCH,
    DELETE
}
