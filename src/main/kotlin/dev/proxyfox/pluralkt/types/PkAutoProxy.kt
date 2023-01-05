package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Instant
import kotlinx.serialization.*

@Serializable
class PkAutoProxy : PkType {
    val lastLatchTimestamp: Instant? = null
    @SerialName("autoproxy_mode")
    var autoProxyMode: PkProxyMode = PkProxyMode.OFF
    var autoProxyMember: PkId? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}