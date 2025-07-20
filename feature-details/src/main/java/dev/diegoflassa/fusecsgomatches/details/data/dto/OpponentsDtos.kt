package dev.diegoflassa.fusecsgomatches.details.data.dto

import android.net.Uri
import dev.diegoflassa.fusecsgomatches.core.data.serializers.UriSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpponentsResponseDto(
    @SerialName("opponents") val opponents: List<OpponentTeamDetailDto> = emptyList()
)

@Serializable
data class OpponentTeamDetailDto(
    @SerialName("name") val name: String? = null,
    @Serializable(with = UriSerializer::class)
    @SerialName("image_url") val imageUrl: Uri? = null,
    @SerialName("players") val players: List<PlayerDetailDto>? = null
)

@Serializable
data class PlayerDetailDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @Serializable(with = UriSerializer::class)
    @SerialName("image_url") val imageUrl: Uri? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null
)
