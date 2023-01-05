package dev.proxyfox.pluralkt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PkPrivacy {
    @SerialName("private")
    PRIVATE,
    @SerialName("public")
    PUBLIC
}
