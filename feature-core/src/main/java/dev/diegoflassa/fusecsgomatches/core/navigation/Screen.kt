package dev.diegoflassa.fusecsgomatches.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable
    data object Main : Screen

    @Serializable
    data object Details : Screen
}
