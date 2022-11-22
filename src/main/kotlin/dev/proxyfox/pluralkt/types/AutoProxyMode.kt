package dev.proxyfox.pluralkt.types

import kotlinx.serialization.Serializable

@Serializable
enum class AutoProxyMode {
    OFF,
    FRONT,
    LATCH,
    MEMBER
}