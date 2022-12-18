package dev.proxyfox.pluralkt.types

import kotlinx.serialization.Serializable

@Serializable
class ApiMessage : ApiType {
    val timestamp: PkTimestamp = ""
    val id: PkSnowflake = ""
    val original: PkSnowflake = ""
    val sender: PkSnowflake = ""
    val channel: PkSnowflake = ""
    val guild: PkSnowflake = ""
    val system: ApiSystem? = null
    val member: ApiMember? = null
}