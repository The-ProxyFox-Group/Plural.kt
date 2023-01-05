package dev.proxyfox.pluralkt.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PkColor.Serializer::class)
class PkColor(var color: Int) : PkType {
    constructor(color: String) : this(if (color == "") -1 else (color.toUIntOrNull(16)?.toInt() ?: Integer.decode(color)) and 0xFFFFFF)

    fun getString(): String? = if (color < 0) null else color.toString(16).run { padStart(6, '0') }

    class Serializer : KSerializer<PkColor> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("color")
        override fun deserialize(decoder: Decoder): PkColor = PkColor(decoder.decodeString())

        @OptIn(ExperimentalSerializationApi::class)
        override fun serialize(encoder: Encoder, value: PkColor) = value.getString()?.let {encoder.encodeString(it)} ?: encoder.encodeNull()
    }

    override fun toString(): String = getString() ?: "000000"
}
