package dev.diegoflassa.fusecsgomatches.details.ui

import dev.diegoflassa.fusecsgomatches.details.data.dto.OpponentsResponseDto

data class DetailsUIState(
    val opponents: OpponentsResponseDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)