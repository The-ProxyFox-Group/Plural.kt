package dev.proxyfox.pluralkt.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias PkId = String
typealias PkReference = String
typealias PkUuid = String
typealias PkSnowflake = String

@Serializable
enum class PkPrivacy {
    @SerialName("private")
    PRIVATE,
    @SerialName("public")
    PUBLIC
}

@Serializable(with = PkColor.Serializer::class)
class PkColor(var color: Int) : ApiType {
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

