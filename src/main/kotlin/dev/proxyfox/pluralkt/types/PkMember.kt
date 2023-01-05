package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*
import java.util.*
import kotlin.collections.ArrayList

@Serializable
class PkMember : PkType {
    val id: PkId = ""
    val uuid: PkUuid = PkUuid(UUID(0,0))
    val created: Instant = Clock.System.now()
    var name: String = ""
    @SerialName("display_name")
    var displayName: String? = null
    var color: PkColor? = null
    var birthday: String? = null
    var pronouns: String? = null
    @SerialName("avatar_url")
    var avatarUrl: String? = null
    var banner: String? = null
    var description: String? = null
    @SerialName("proxy_tags")
    var proxyTags: ArrayList<PkProxyTag> = arrayListOf()
    @SerialName("keep_proxy")
    var keepProxy: Boolean = false
    @SerialName("autoproxy_enabled")
    var autoProxyEnabled: Boolean? = null
    @SerialName("message_count")
    val messageCount: Int? = null
    @SerialName("last_message_timestamp")
    val lastMessage: Instant? = null
    var privacy: PkMemberPrivacy? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}
