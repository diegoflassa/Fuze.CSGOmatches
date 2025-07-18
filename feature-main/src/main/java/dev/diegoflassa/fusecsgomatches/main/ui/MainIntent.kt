package dev.diegoflassa.fusecsgomatches.main.ui

import dev.diegoflassa.fusecsgomatches.main.data.network.dto.MatchDto

sealed class MainIntent {
    object LoadMatches : MainIntent()
    object RefreshMatches : MainIntent()
    data class OnMatchClicked(val matchId: Long) : MainIntent()
}