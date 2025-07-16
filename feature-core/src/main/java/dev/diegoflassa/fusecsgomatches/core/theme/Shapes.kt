package dev.diegoflassa.fusecsgomatches.core.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape

val LocalFuseCSGOMatchesShapes = staticCompositionLocalOf { FuseCSGOMatchesShapes() }

data class FuseCSGOMatchesShapes(
    val placeholder: Shape = CircleShape,
)
