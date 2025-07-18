package dev.diegoflassa.fusecsgomatches.main.ui

sealed class MainEffect {
    data class ShowError(val message: String) : MainEffect()
    data class NavigateToDetails(val matchIdOrSlug: String) : MainEffect()
}