package dev.proxyfox.pluralkt

import dev.proxyfox.pluralkt.types.*

interface Response<T> {
    fun isSuccess(): Boolean

    fun getSuccess(): T

    fun isError(): Boolean

    fun getError(): PkError

    override fun toString(): String
}

class ResponseSuccess<T>(private val value: T) : Response<T> {
    override fun isSuccess(): Boolean = true

    override fun getSuccess(): T = value

    override fun isError(): Boolean = false

    override fun getError(): PkError = throw IllegalStateException("Response is not an error")

    override fun toString(): String = value.toString()
}

class ResponseError<T>(private val error: PkError) : Response<T> {
    override fun isSuccess(): Boolean = false

    override fun getSuccess(): T = throw IllegalStateException("Response is not a success")

    override fun isError(): Boolean = true

    override fun getError(): PkError = error

    override fun toString(): String = error.toString()
}

class ResponseNull<T> : Response<T> {
    override fun isSuccess(): Boolean = false

    override fun getSuccess(): T = throw IllegalStateException("Response is not a success")

    override fun isError(): Boolean = false

    override fun getError(): PkError = throw IllegalStateException("Response is not an error")

    override fun toString(): String = "null"
}
