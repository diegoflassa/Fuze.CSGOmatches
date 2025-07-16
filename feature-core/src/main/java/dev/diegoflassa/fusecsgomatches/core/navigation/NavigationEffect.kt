package dev.diegoflassa.fusecsgomatches.core.navigation

sealed interface NavigationEffect {
    data class ShowToast(val message: String) : NavigationEffect
    // Add other effects here if needed in the future
}