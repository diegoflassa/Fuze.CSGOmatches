package dev.diegoflassa.fuzecsgomatches.core.data.serializers

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UriSerializer : KSerializer<Uri> {
    override val descriptor = PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Uri {
        val uriString = decoder.decodeString()
        val ret = when {
            uriString.isEmpty() -> Uri.EMPTY
            else -> uriString.toUri()
        }
        return ret
    }

    override fun serialize(encoder: Encoder, value: Uri) {
        val ret = value.toString()
        encoder.encodeString(ret)
    }
}