package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class ApiMember : ApiType {
    val id: PkId = ""
    val uuid: PkUuid = ""
    val created: PkTimestamp = ""
    var name: String = ""
    @SerialName("display_name")
    var displayName: String? = null
    var color: PkColor = null
    var birthday: String? = null
    var pronouns: String? = null
    @SerialName("avatar_url")
    var avatarUrl: String? = null
    var banner: String? = null
    var description: String? = null
    @SerialName("proxy_tags")
    var proxyTags: ArrayList<ApiProxyTag> = arrayListOf()
    @SerialName("keep_proxy")
    var keepProxy: Boolean = false
    @SerialName("autoproxy_enabled")
    var autoProxyEnabled: Boolean? = null
    @SerialName("message_count")
    val messageCount: Int? = null
    @SerialName("last_message_timestamp")
    val lastMessage: PkTimestamp? = null
    var privacy: MemberPrivacy? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}
