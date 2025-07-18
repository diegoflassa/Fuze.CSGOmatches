package dev.diegoflassa.fusecsgomatches.main.ui

sealed class MainIntent {
    object LoadMatches : MainIntent()
    object RefreshMatches : MainIntent()
    data class OnMatchClicked(val matchIdOrSlug: String) : MainIntent()
}