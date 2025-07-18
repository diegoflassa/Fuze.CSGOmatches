package dev.diegoflassa.fusecsgomatches.main.ui

import dev.diegoflassa.fusecsgomatches.main.data.network.dto.MatchDto

sealed class MainEffect {
    data class ShowError(val message: String) : MainEffect()
    data class NavigateToDetails(val matchId: Long) : MainEffect()
}