package dev.diegoflassa.fuzecsgomatches.core.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeParseException
import java.time.Instant

object InstantSerializer : KSerializer<Instant?> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant? {
        val dateString = decoder.decodeString()
        return if (dateString.isEmpty()) {
            null
        } else {
            try {
                Instant.parse(dateString)
            } catch (e: DateTimeParseException) {
                System.err.println("Failed to parse Instant from JSON: $dateString. Error: ${e.message}")
                throw e
            }
        }
    }

    override fun serialize(encoder: Encoder, value: Instant?) {
        encoder.encodeString(value.toString())
    }
}