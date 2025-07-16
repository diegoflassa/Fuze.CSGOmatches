package dev.diegoflassa.fusecsgomatches.ui

sealed interface DetailsEffect {
    data object Placeholder : DetailsEffect
}