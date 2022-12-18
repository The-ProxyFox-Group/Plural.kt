package dev.proxyfox.pluralkt.types

import kotlinx.serialization.*

@Serializable
class ApiError : ApiType {
    val code: Int = 0
    val message: String = ""
    val errors: HashMap<String, ErrorObject>? = null
    @SerialName("retry_after")
    val retryAfter: Int? = null
}