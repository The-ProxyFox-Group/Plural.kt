package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*

@Serializable
class ApiAutoProxy : ApiType {
    val lastLatchTimestamp: Instant? = null
    @SerialName("autoproxy_mode")
    var autoProxyMode: AutoProxyMode = AutoProxyMode.OFF
    var autoProxyMember: PkId? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}