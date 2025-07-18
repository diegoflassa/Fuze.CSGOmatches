package dev.diegoflassa.fusecsgomatches.details.data.dto

import android.net.Uri
import kotlinx.serialization.SerialName

data class MatchOpponentsResponseDto(
    @SerialName("opponent_type")
    val opponentType: String,
    @SerialName("opponents")
    val opponents: List<OpponentTeamDetailDto>
)

data class OpponentTeamDetailDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("location")
    val location: String?,
    @SerialName("players")
    val players: List<PlayerDetailDto>,
    @SerialName("slug")
    val slug: String,
    @SerialName("modified_at")
    val modifiedAt: String,
    @SerialName("acronym")
    val acronym: String?,
    @SerialName("image_url")
    val imageUrl: Uri?,
    @SerialName("current_videogame")
    val currentVideogame: VideogameDto
)


data class VideogameDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("slug")
    val slug: String
)

data class PlayerDetailDto(
    @SerialName("active")
    val active: Boolean,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("role")
    val role: String?,
    @SerialName("slug")
    val slug: String,
    @SerialName("modified_at")
    val modifiedAt: String,
    @SerialName("birthday")
    val birthday: String?,
    @SerialName("first_name")
    val firstName: String?,
    @SerialName("last_name")
    val lastName: String?,
    @SerialName("nationality")
    val nationality: String?,
    @SerialName("age")
    val age: Int?,
    @SerialName("image_url")
    val imageUrl: Uri?
)
