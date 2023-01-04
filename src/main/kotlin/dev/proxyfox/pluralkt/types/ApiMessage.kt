package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*

@Serializable
class ApiMessage : ApiType {
    val timestamp: Instant = Clock.System.now()
    val id: PkSnowflake = ""
    val original: PkSnowflake = ""
    val sender: PkSnowflake = ""
    val channel: PkSnowflake = ""
    val guild: PkSnowflake = ""
    val system: ApiSystem? = null
    val member: ApiMember? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}