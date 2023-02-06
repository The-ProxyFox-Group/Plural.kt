package dev.proxyfox.pluralkt.dispatch.types

import dev.proxyfox.pluralkt.types.PkId
import dev.proxyfox.pluralkt.types.PkReference
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
@SerialName("UPDATE_SYSTEM")
class SystemUpdateEvent : Event {
    constructor(
        signingToken: String,
        systemId: PkId?,
        referencedData: PkReference?,
        data: JsonObject?
    ) : super(signingToken, systemId, referencedData, data)
}
