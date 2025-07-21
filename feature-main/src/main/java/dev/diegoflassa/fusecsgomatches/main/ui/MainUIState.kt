package dev.diegoflassa.fusecsgomatches.main.ui

import androidx.paging.PagingData
import dev.diegoflassa.fusecsgomatches.main.data.dto.MatchDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MainUIState(
    val matchesFlow: Flow<PagingData<MatchDto>> = emptyFlow(),
    val onlyFutureGames: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
