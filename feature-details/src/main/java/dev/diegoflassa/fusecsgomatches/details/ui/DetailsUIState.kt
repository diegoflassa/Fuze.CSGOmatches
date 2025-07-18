package dev.diegoflassa.fusecsgomatches.details.ui

import dev.diegoflassa.fusecsgomatches.details.data.dto.MatchOpponentsResponseDto

data class DetailsUIState(
    val opponents: MatchOpponentsResponseDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)