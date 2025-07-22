package dev.diegoflassa.fuzecsgomatches.core.data.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MatchStatus() {
    UNKNOWN,
    @SerialName("finished")
    FINISHED,
    @SerialName("ended")
    ENDED,
    @SerialName("canceled")
    CANCELED,
    @SerialName("in_progress")
    IN_PROGRESS,
    @SerialName("scheduled")
    SCHEDULED,
    @SerialName("not_started")
    NOT_STARTED;
}