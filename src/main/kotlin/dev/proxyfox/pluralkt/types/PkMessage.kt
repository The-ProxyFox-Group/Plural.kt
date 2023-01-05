package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*

@Serializable
class PkMessage : PkType {
    val timestamp: Instant = Clock.System.now()
    val id: PkSnowflake = ""
    val original: PkSnowflake = ""
    val sender: PkSnowflake = ""
    val channel: PkSnowflake = ""
    val guild: PkSnowflake = ""
    val system: PkSystem? = null
    val member: PkMember? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}