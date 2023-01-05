package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class PkSystemSettings : PkType {
    @SerialName("member_limit")
    val memberLimit: Int = 1000
    @SerialName("group_limit")
    val groupLimit: Int = 250
    var timezone: String = "UTC"
    @SerialName("pings_enabled")
    var pingsEnabled: Boolean = true
    @SerialName("member_default_private")
    var memberDefaultPrivate: Boolean = false
    @SerialName("group_default_private")
    var groupDefaultPrivate: Boolean = false
    @SerialName("show_private_info")
    var showPrivateInfo: Boolean = false

    override fun toString(): String = PluralKt.json.encodeToString(this)
}