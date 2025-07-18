@file:Suppress("unused")

package dev.diegoflassa.fusecsgomatches.core.data.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class InstantJsonAdapter {
    private val formatter = DateTimeFormatter.ISO_INSTANT

    @ToJson
    fun toJson(instant: Instant?): String? {
        return instant?.let { formatter.format(it) }
    }

    @FromJson
    fun fromJson(dateString: String?): Instant? {
        return if (dateString.isNullOrEmpty()) {
            null
        } else {
            try {
                Instant.parse(dateString)
            } catch (e: DateTimeParseException) {
                System.err.println("Failed to parse Instant from JSON: $dateString. Error: ${e.message}")
                null
            }
        }
    }
}
