package dev.proxyfox.pluralkt.types

import kotlinx.serialization.*

@Serializable
class ErrorObject {
    val message: String = ""
    @SerialName("max_length")
    val maxLength: Int? = null
    @SerialName("actual_length")
    val actualLength: Int? = null
}