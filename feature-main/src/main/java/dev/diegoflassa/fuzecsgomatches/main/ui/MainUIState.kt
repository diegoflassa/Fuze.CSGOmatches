package dev.diegoflassa.fuzecsgomatches.main.ui

import androidx.paging.PagingData
import dev.diegoflassa.fuzecsgomatches.main.data.dto.MatchDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MainUIState(
    val onlyFutureGames: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
