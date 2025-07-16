package dev.diegoflassa.fusecsgomatches.core.navigation

data class NavigationUIState(
    val backStack: List<Screen> = listOf(Screen.Main)
)