package dev.diegoflassa.fusecsgomatches.details.ui


sealed interface DetailsEffect {
    data object Placeholder : DetailsEffect
}