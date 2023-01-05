package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*

@Serializable
class PkMessage : PkType {
    val timestamp: Instant = Clock.System.now()
    val id: PkSnowflake = PkSnowflake(0UL)
    val original: PkSnowflake = PkSnowflake(0UL)
    val sender: PkSnowflake = PkSnowflake(0UL)
    val channel: PkSnowflake = PkSnowflake(0UL)
    val guild: PkSnowflake = PkSnowflake(0UL)
    val system: PkSystem? = null
    val member: PkMember? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}