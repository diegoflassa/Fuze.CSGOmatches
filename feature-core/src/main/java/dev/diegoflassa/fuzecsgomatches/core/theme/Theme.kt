package dev.diegoflassa.fuzecsgomatches.core.theme

import android.app.Activity
import android.os.Build
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
object FuzeCSGOMatchesTheme {
    val colorScheme: ColorScheme
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme

    val baselineTypography: Typography
        @Composable @ReadOnlyComposable get() = LocalBaselineTypography.current

    val typography: FuzeCSGOMatchesTypography
        @Composable @ReadOnlyComposable get() = LocalFuzeCSGOMatchesTypography.current

    val shapes: FuzeCSGOMatchesShapes
        @Composable @ReadOnlyComposable get() = LocalFuzeCSGOMatchesShapes.current

    val dimen: FuzeCSGOMatchesDimen
        @Composable @ReadOnlyComposable get() = LocalFuzeCSGOMatchesDimen.current
}

@Composable
fun FuzeCSGOMatchesThemeContent(content: @Composable () -> Unit) {
    val typography: Typography = LocalFuzeCSGOMatchesTypography.current.typography
    val colorScheme = fuzeCSGOMatchesColorScheme
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
