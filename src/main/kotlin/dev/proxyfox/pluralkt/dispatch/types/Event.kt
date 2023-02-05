package dev.proxyfox.pluralkt.dispatch.types

import dev.proxyfox.pluralkt.types.PkId
import dev.proxyfox.pluralkt.types.PkReference
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Event(
    @SerialName("signing_token")
    val signingToken: String,
    @SerialName("system_id")
    val systemId: PkId,
    @SerialName("id")
    val referencedData: PkReference?
)
