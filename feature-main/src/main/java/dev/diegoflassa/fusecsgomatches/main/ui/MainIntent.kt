package dev.diegoflassa.fusecsgomatches.main.ui

sealed class MainIntent {
    object LoadMatches : MainIntent()
    data class OnMatchClicked(
        val matchIdOrSlug: String,
        val leagueName: String?,
        val serieFullName: String?,
        val scheduledAt: String?,
    ) : MainIntent()
}