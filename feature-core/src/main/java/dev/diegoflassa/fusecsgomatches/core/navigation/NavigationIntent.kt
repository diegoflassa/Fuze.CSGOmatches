package dev.diegoflassa.fusecsgomatches.core.navigation

sealed interface NavigationIntent {
    data class NavigateTo(val screen: Screen) : NavigationIntent
    data object GoToMain : NavigationIntent
    data object GoBack : NavigationIntent
    data class ReplaceAll(val screen: Screen) : NavigationIntent
}