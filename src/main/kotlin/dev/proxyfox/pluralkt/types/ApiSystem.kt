package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class ApiSystem : ApiType {
    val id: PkId = ""
    val uuid: PkUuid = ""
    val created: PkTimestamp = ""
    var name: String? = null
    var description: String? = null
    var tag: String? = null
    var pronouns: String? = null
    @SerialName("avatar_url")
    var avatarUrl: String? = null
    var banner: String? = null
    var color: PkColor = null
    var privacy: SystemPrivacy? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}