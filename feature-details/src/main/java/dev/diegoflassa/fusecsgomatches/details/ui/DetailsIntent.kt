package dev.diegoflassa.fusecsgomatches.details.ui

sealed class DetailsIntent {
    data class LoadDetails(val matchIdOrSlug: String) : DetailsIntent()
    object Refresh : DetailsIntent()
    object NavigateToMain : DetailsIntent()
}