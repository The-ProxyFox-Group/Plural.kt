package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class MemberPrivacy : ApiType {
    var visibility: PkPrivacy? = null
    @SerialName("name_privacy")
    var name: PkPrivacy? = null
    @SerialName("description_privacy")
    var description: PkPrivacy? = null
    @SerialName("birthday_privacy")
    var birthday: PkPrivacy? = null
    @SerialName("pronoun_privacy")
    var pronoun: PkPrivacy? = null
    @SerialName("avatar_privacy")
    var avatar: PkPrivacy? = null
    @SerialName("metadata_privacy")
    var metadata: PkPrivacy? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}