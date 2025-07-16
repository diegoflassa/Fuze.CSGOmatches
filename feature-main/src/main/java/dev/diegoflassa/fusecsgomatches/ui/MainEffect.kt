package dev.diegoflassa.fusecsgomatches.ui

sealed interface MainEffect {
    data object Placeholder : MainEffect
}