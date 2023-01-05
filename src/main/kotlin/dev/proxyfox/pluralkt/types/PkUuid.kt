package dev.proxyfox.pluralkt.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

@JvmInline
@Serializable(with = PkUuid.Serializer::class)
value class PkUuid(val uuid: UUID) : PkType {
    constructor(value: String) : this(UUID.fromString(value))

    class Serializer : KSerializer<PkUuid> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("uuid")
        override fun deserialize(decoder: Decoder): PkUuid = PkUuid(decoder.decodeString())

        override fun serialize(encoder: Encoder, value: PkUuid) = encoder.encodeString(value.toString())
    }

    override fun toString(): String = uuid.toString()
}
