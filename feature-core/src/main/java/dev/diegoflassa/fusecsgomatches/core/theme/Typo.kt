@file:Suppress("unused")

package dev.diegoflassa.fusecsgomatches.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf

val LocalFuseCSGOMatchesTypography = staticCompositionLocalOf { FuseCSGOMatchesTypography() }

val LocalBaselineTypography = staticCompositionLocalOf { Typography() }

class FuseCSGOMatchesTypography(
    val typography: Typography = Typography(),
)
