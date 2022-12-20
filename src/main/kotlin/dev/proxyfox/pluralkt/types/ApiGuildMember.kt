package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class ApiGuildMember : ApiType {
    @SerialName("guild_id")
    val guildId: PkSnowflake = ""
    @SerialName("display_name")
    var displayName: String? = null
    @SerialName("avatar_url")
    var avatarUrl: String? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}