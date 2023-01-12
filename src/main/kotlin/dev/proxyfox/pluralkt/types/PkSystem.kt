package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*
import java.util.*

@Serializable
class PkSystem : PkType {
    val id: PkId = ""
    val uuid: PkUuid = PkUuid(UUID(0,0))
    val created: Instant = Clock.System.now()
    var name: String? = null
    var description: String? = null
    var tag: String? = null
    var pronouns: String? = null
    @SerialName("avatar_url")
    var avatarUrl: String? = null
    var banner: String? = null
    var color: PkColor? = null
    var privacy: PkSystemPrivacy? = null
    @SerialName("webhook_url")
    var webhookUrl: String? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}
