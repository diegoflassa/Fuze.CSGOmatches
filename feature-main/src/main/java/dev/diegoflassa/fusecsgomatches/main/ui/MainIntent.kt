package dev.diegoflassa.fusecsgomatches.main.ui

sealed class MainIntent {
    object LoadMatches : MainIntent()
    object ShowFilter : MainIntent()
    data class ApplyFilter(
        val onlyFutureEvents: Boolean,
    ) : MainIntent()

    data class OnMatchClicked(
        val matchIdOrSlug: String,
        val leagueName: String?,
        val serieFullName: String?,
        val scheduledAt: String?,
    ) : MainIntent()
}