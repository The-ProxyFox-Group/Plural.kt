package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
enum class PkProxyMode {
    @SerialName("off") OFF,
    @SerialName("front") FRONT,
    @SerialName("latch") LATCH,
    @SerialName("member") MEMBER
}