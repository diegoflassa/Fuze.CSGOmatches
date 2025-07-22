package dev.diegoflassa.fuzecsgomatches.details.ui

sealed class DetailsIntent {
    data class LoadDetails(val matchIdOrSlug: String) : DetailsIntent()
    object Refresh : DetailsIntent()
    object NavigateToMain : DetailsIntent()
}