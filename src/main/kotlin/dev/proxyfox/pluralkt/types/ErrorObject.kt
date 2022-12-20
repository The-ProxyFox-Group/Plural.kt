package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class ErrorObject : ApiType {
    val message: String = ""
    @SerialName("max_length")
    val maxLength: Int? = null
    @SerialName("actual_length")
    val actualLength: Int? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}