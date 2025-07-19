package dev.diegoflassa.fusecsgomatches.details.data.dto

import android.net.Uri
import dev.diegoflassa.fusecsgomatches.core.data.serializers.InstantSerializer
import dev.diegoflassa.fusecsgomatches.core.data.serializers.UriSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class OpponentsResponseDto(
    @SerialName("opponent_type") val opponentType: String? = null,
    @SerialName("opponents") val opponents: List<OpponentTeamDetailDto> = emptyList()
)

@Serializable
data class OpponentTeamDetailDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("players") val players: List<PlayerDetailDto>? = null,
    @SerialName("slug") val slug: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("modified_at") val modifiedAt: Instant? = null,
    @SerialName("acronym") val acronym: String? = null,
    @Serializable(with = UriSerializer::class)
    @SerialName("image_url") val imageUrl: Uri? = null,
    @SerialName("current_videogame") val currentVideogame: VideogameDto? = null
)

@Serializable
data class VideogameDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("slug") val slug: String? = null
)

@Serializable
data class PlayerDetailDto(
    @SerialName("active") val active: Boolean? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("role") val role: String? = null,
    @SerialName("slug") val slug: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("modified_at") val modifiedAt: Instant? = null,
    @SerialName("birthday") val birthday: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("nationality") val nationality: String? = null,
    @SerialName("age") val age: Int? = null,
    @Serializable(with = UriSerializer::class)
    @SerialName("image_url") val imageUrl: Uri? = null
)
