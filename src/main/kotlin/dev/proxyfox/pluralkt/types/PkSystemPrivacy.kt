package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class PkSystemPrivacy : PkType {
    @SerialName("description_privacy")
    var description: PkPrivacy? = null
    @SerialName("pronoun_privacy")
    var pronoun: PkPrivacy? = null
    @SerialName("member_list_privacy")
    var memberList: PkPrivacy? = null
    @SerialName("group_list_privacy")
    var groupList: PkPrivacy? = null
    @SerialName("front_privacy")
    var front: PkPrivacy? = null
    @SerialName("front_history_privacy")
    var frontHistory: PkPrivacy? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}