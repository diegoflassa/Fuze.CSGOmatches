package dev.diegoflassa.fusecsgomatches.details.ui

sealed interface DetailsIntent {
    data object Placeholder : DetailsIntent
}