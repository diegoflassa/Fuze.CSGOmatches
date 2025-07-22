package dev.diegoflassa.fuzecsgomatches.core.navigation

data class NavigationUIState(
    val backStack: List<Screen> = listOf(Screen.Main)
)