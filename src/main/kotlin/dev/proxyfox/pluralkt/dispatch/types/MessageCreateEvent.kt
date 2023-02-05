package dev.proxyfox.pluralkt.dispatch.types

import dev.proxyfox.pluralkt.types.PkId
import dev.proxyfox.pluralkt.types.PkReference
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
@SerialName("CREATE_MESSAGE")
class MessageCreateEvent : Event {
    constructor(
        signingToken: String,
        systemId: PkId?,
        referencedData: PkReference?,
        data: JsonObject?
    ) : super(signingToken, systemId, referencedData, data)
}