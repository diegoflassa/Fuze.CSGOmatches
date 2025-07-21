package dev.diegoflassa.fusecsgomatches.main.ui

sealed class MainEffect {
    data class ShowError(val message: String) : MainEffect()
    object ShowFilter : MainEffect()
    data class NavigateToDetails(
        val matchIdOrSlug: String,
        val leagueName: String?,
        val serieFullName: String?,
        val scheduledAt: String?,
    ) : MainEffect()
}