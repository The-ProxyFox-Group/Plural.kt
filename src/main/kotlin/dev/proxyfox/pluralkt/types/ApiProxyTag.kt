package dev.proxyfox.pluralkt.types

import kotlinx.serialization.Serializable

@Serializable
class ApiProxyTag : ApiType {
    var prefix: String? = null
    var suffix: String? = null
}