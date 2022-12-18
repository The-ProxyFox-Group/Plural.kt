package dev.proxyfox.pluralkt.types

import kotlinx.serialization.*

@Serializable
class ApiAutoProxy : ApiType {
    val lastLatchTimestamp: PkTimestamp? = null
    @SerialName("autoproxy_mode")
    var autoProxyMode: AutoProxyMode = AutoProxyMode.OFF
    var autoProxyMember: PkId? = null
}