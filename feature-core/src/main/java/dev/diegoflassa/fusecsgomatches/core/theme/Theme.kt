package dev.diegoflassa.fusecsgomatches.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Suppress("unused")
object FuseCSGOMatchesTheme {
    val colorScheme: ColorScheme
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme

    val baselineTypography: Typography
        @Composable @ReadOnlyComposable get() = LocalBaselineTypography.current

    val typography: FuseCSGOMatchesTypography
        @Composable @ReadOnlyComposable get() = LocalFuseCSGOMatchesTypography.current

    val shapes: FuseCSGOMatchesShapes
        @Composable @ReadOnlyComposable get() = LocalFuseCSGOMatchesShapes.current

    val dimen: FuseCSGOMatchesDimen
        @Composable @ReadOnlyComposable get() = LocalFuseCSGOMatchesDimen.current
}

@Composable
fun FuseCSGOMatchesThemeContent(content: @Composable () -> Unit) {
    val typography: Typography = LocalFuseCSGOMatchesTypography.current.typography
    val colorScheme = fuseCSGOMatchesColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            WindowCompat.setDecorFitsSystemWindows(window, false)

            if (Build.VERSION.SDK_INT < 35) {
                @Suppress("DEPRECATION")
                window.statusBarColor = colorScheme.primary.toArgb()
            }

            insetsController.isAppearanceLightStatusBars = false

            if (Build.VERSION.SDK_INT < 35) {
                @Suppress("DEPRECATION")
                window.navigationBarColor = colorScheme.surface.toArgb()
            }
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
