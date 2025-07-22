package dev.diegoflassa.fuzecsgomatches.details.ui


sealed class DetailsEffect {
    object NavigateToMain : DetailsEffect()
    data class ShowError(val message: String) : DetailsEffect()
}