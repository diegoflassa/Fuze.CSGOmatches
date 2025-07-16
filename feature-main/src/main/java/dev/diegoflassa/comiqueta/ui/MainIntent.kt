package dev.diegoflassa.fusecsgomatches.ui

sealed interface MainIntent {
    data object Placeholder : MainIntent
}