package dev.proxyfox.pluralkt.dispatch.types

import dev.proxyfox.pluralkt.types.PkId
import dev.proxyfox.pluralkt.types.PkReference
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PING")
class PingEvent : Event {
    constructor(
        signingToken: String,
        systemId: PkId,
        referencedData: PkReference?
    ) : super(signingToken, systemId, referencedData)
}
