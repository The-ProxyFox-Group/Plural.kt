package dev.proxyfox.pluralkt

import dev.proxyfox.pluralkt.types.*
import io.ktor.serialization.*

interface Response<T> {
    fun isSuccess(): Boolean

    fun getSuccess(): T

    fun isError(): Boolean

    fun getError(): PkError

    fun getException(): JsonConvertException?

    override fun toString(): String
}

class ResponseSuccess<T>(private val value: T) : Response<T> {
    override fun isSuccess(): Boolean = true

    override fun getSuccess(): T = value

    override fun isError(): Boolean = false

    override fun getError(): PkError = throw IllegalStateException("Response is not an error")
    override fun getException(): JsonConvertException? = null

    override fun toString(): String = value.toString()
}

class ResponseError<T>(private val error: PkError, private val exception: JsonConvertException? = null) : Response<T> {
    override fun isSuccess(): Boolean = false

    override fun getSuccess(): T = throw IllegalStateException("Response is not a success")

    override fun isError(): Boolean = true

    override fun getError(): PkError = error

    override fun getException(): JsonConvertException? = exception

    override fun toString(): String = error.toString()
}

class ResponseNull<T>(private val exception: JsonConvertException) : Response<T> {
    override fun isSuccess(): Boolean = false

    override fun getSuccess(): T = throw IllegalStateException("Response is not a success")

    override fun isError(): Boolean = false

    override fun getError(): PkError = throw IllegalStateException("Response is not an error")

    override fun getException(): JsonConvertException = exception

    override fun toString(): String = "null"
}
