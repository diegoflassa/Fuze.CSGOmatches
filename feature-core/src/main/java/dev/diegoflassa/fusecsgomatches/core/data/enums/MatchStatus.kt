package dev.diegoflassa.fusecsgomatches.core.data.enums

import java.util.Locale.getDefault

enum class MatchStatus() {
    UNKNOWN,
    ENDED,
    IN_PROGRESS,
    SCHEDULED;

    companion object {
        fun fromString(value: String): MatchStatus {
            val lowerCaseValue = value.lowercase(getDefault())
            return when {
                lowerCaseValue.contains("ended") -> ENDED
                lowerCaseValue.contains("in progress") -> IN_PROGRESS
                lowerCaseValue.contains("scheduled") -> SCHEDULED
                else -> UNKNOWN
            }
        }
    }
}