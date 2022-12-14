package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*
import java.util.*

@Serializable
class PkGroup : PkType {
    val id: PkId = ""
    val uuid: PkUuid = PkUuid(UUID(0,0))
    val created: Instant = Clock.System.now()
    var name: String = ""
    @SerialName("display_name")
    var displayName: String? = null
    var color: PkColor? = null
    var icon: String? = null
    var banner: String? = null
    var description: String? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}