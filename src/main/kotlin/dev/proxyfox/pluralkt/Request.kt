package dev.proxyfox.pluralkt

import dev.proxyfox.pluralkt.types.*
import io.ktor.util.reflect.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class Request<I, O>(val endpoint: String, val type: RequestType, val token: String?, val data: I?, val outputTypeInfo: TypeInfo, val inputTypeInfo: TypeInfo) {
    var onComplete: Response<O>.() -> Unit = {}
    fun createUrl(baseUrl: String) = baseUrl+endpoint

    fun onComplete(action: Response<O>.() -> Unit) {
        onComplete = action
    }
    fun future(): Future<Response<O>> {
        val future = CompletableFuture<Response<O>>()
        onComplete {
            future.complete(this)
        }
        return future
    }
    fun await(): Response<O> {
        return future().get()
    }
}

inline fun <reified O> get(endpoint: String, token: String? = null) = Request<PkType, O>(endpoint, RequestType.GET, token, null, typeInfo<O>(), typeInfo<PkType>())
inline fun <reified I, reified O> post(endpoint: String, data: I? = null, token: String) = Request<I, O>(endpoint, RequestType.POST, token, data, typeInfo<O>(), typeInfo<I>())
inline fun <reified I, reified O> patch(endpoint: String, data: I? = null, token: String) = Request<I, O>(endpoint, RequestType.PATCH, token, data, typeInfo<O>(), typeInfo<I>())
inline fun <reified O> delete(endpoint: String, token: String) = Request<PkType, O>(endpoint, RequestType.DELETE, token, null, typeInfo<O>(), typeInfo<PkType>())

enum class RequestType {
    GET,
    POST,
    PATCH,
    DELETE
}
