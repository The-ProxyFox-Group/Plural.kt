package dev.proxyfox.pluralkt

import dev.proxyfox.pluralkt.types.*

interface Response<T : ApiType> {
    fun isSuccess(): Boolean

    fun getSuccess(): T

    fun isError(): Boolean

    fun getError(): ApiError
}

class ResponseSuccess<T : ApiType>(private val value: T) : Response<T> {
    override fun isSuccess(): Boolean = true

    override fun getSuccess(): T = value

    override fun isError(): Boolean = false

    override fun getError(): ApiError = throw IllegalStateException("Response is not an error")
}

class ResponseError<T : ApiType>(private val error: ApiError) : Response<T> {
    override fun isSuccess(): Boolean = true

    override fun getSuccess(): T = throw IllegalStateException("Response is not a success")

    override fun isError(): Boolean = true

    override fun getError(): ApiError = error
}
