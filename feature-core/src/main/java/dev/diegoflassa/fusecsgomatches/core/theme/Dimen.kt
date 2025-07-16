package dev.diegoflassa.fusecsgomatches.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalFuseCSGOMatchesDimen = staticCompositionLocalOf { FuseCSGOMatchesDimen() }

data class FuseCSGOMatchesDimen(
    val noPadding: Dp = 0.dp,
)
