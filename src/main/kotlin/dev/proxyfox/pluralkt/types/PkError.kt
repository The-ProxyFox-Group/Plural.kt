package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class PkError : PkType {
    val code: Int = 0
    val message: String = ""
    val errors: HashMap<String, PkErrorMessage>? = null
    @SerialName("retry_after")
    val retryAfter: Int? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}