package dev.diegoflassa.fuzecsgomatches.details.ui

import dev.diegoflassa.fuzecsgomatches.details.data.dto.OpponentsResponseDto

data class DetailsUIState(
    val opponents: OpponentsResponseDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)