package dev.diegoflassa.fuzecsgomatches.main.data.dto

import android.net.Uri
import dev.diegoflassa.fuzecsgomatches.core.data.enums.MatchStatus
import dev.diegoflassa.fuzecsgomatches.core.data.serializers.InstantSerializer
import dev.diegoflassa.fuzecsgomatches.core.data.serializers.UriSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class MatchDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("begin_at") val beginAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("scheduled_at") val scheduledAt: Instant? = null,
    @SerialName("status") val status: MatchStatus? = MatchStatus.UNKNOWN,
    @SerialName("slug") val slug: String? = null,
    @SerialName("league") val league: LeagueDto? = null,
    @SerialName("serie") val serie: SerieDto? = null,
    @SerialName("opponents") val opponents: List<OpponentWrapperDto?>? = null,
    @SerialName("live") val live: LiveDto? = null
)

@Serializable
data class OpponentWrapperDto(
    @SerialName("opponent") val opponent: OpponentDetailsDto? = null
)

@Serializable
data class OpponentDetailsDto(
    @SerialName("name") val name: String? = null,
    @Serializable(with = UriSerializer::class)
    @SerialName("image_url") val imageUrl: Uri? = null
)

@Serializable
data class LeagueDto(
    @SerialName("name") val name: String? = null,
    @Serializable(with = UriSerializer::class)
    @SerialName("image_url") val imageUrl: Uri? = null
)

@Serializable
data class SerieDto(
    @SerialName("name") val name: String? = null,
    @SerialName("full_name") val fullName: String? = null
)

@Serializable
data class LiveDto(
    @SerialName("supported") val supported: Boolean? = null
)
