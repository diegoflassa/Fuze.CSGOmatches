@file:Suppress("unused")

package dev.diegoflassa.fusecsgomatches.core.data.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import dev.diegoflassa.fusecsgomatches.core.data.enums.MatchStatus

class MatchStatusJsonAdapter {

    @ToJson
    fun toJson(status: MatchStatus?): String? {
        return status?.name
    }

    @FromJson
    fun fromJson(status: String?): MatchStatus? {
        return MatchStatus.fromString(status ?: "")
    }
}
