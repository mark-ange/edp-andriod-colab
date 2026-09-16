package com.example.myapplication.data.network.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.time.Instant
import java.time.format.DateTimeParseException

@Serializable
data class MessageDto(
    val id: String? = null,
    val sender: String? = null,
    val text: String? = null,
    @Serializable(with = CreatedAtSerializer::class)
    val createdAt: Long? = null
)

@Serializable
data class NewMessageDto(
    val sender: String,
    val text: String,
    val createdAt: Long
)

object CreatedAtSerializer : KSerializer<Long?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CreatedAt", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Long? {
        val input = decoder as? JsonDecoder ?: return try { decoder.decodeLong() } catch (e: Exception) { null }
        val primitive = input.decodeJsonElement() as? JsonPrimitive ?: return null
        
        return if (primitive.isString) {
            try {
                Instant.parse(primitive.content).toEpochMilli()
            } catch (e: DateTimeParseException) {
                null
            }
        } else {
            primitive.longOrNull
        }
    }

    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Long?) {
        if (value == null) encoder.encodeNull() else encoder.encodeLong(value)
    }
}
