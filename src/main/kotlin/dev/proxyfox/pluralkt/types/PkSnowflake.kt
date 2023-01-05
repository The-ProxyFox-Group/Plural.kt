package dev.proxyfox.pluralkt.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@JvmInline
@Serializable(with = PkSnowflake.Serializer::class)
value class PkSnowflake(val value: ULong) : PkType {
    constructor(value: String) : this(value.toULong())

    class Serializer : KSerializer<PkSnowflake> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("snowflake")
        override fun deserialize(decoder: Decoder): PkSnowflake = PkSnowflake(decoder.decodeString())

        override fun serialize(encoder: Encoder, value: PkSnowflake) = encoder.encodeString(value.toString())
    }

    override fun toString(): String = value.toString()
}
