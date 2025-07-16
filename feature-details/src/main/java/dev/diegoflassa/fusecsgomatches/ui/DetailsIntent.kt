package dev.diegoflassa.fusecsgomatches.ui

sealed interface DetailsIntent {
    data object Placeholder : DetailsIntent
}