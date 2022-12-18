package dev.proxyfox.pluralkt.types

import kotlinx.serialization.*

@Serializable
class ApiGuildSystem : ApiType {
    @SerialName("guild_id")
    val guildId: PkSnowflake = ""
    @SerialName("proxying_enabled")
    var proxyingEnabled: Boolean = true
    var tag: String? = null
    @SerialName("tag_enabled")
    var tagEnabled: Boolean = true
}