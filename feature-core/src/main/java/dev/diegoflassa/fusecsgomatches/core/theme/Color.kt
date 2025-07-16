package dev.diegoflassa.fusecsgomatches.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

object FuseCSGOMatchesColors {
    val primaryDark = Color(0xFF161621)//Ok
    val onPrimaryDark = Color(0xFFFFFFFF)//Ok
    val primaryContainerDark = Color(0xFF161621)//Ok
    val onPrimaryContainerDark = Color(0xFFFFFFFF)//Ok
    val secondaryDark = Color(0xFF272639)//Ok
    val onSecondaryDark = Color(0xFFFFFFFF)//Ok
    val secondaryContainerDark = Color(0xFF272639)//Ok
    val onSecondaryContainerDark = Color(0xFFFFFFFF)//Ok
    val tertiaryDark = Color(0xFF00FF00)
    val onTertiaryDark = Color(0xFF0000FF)
    val tertiaryContainerDark = Color(0xFFFF0000)
    val onTertiaryContainerDark = Color(0xFF00FF00)
    val errorDark = Color(0xFFFFB4AB)//Ok
    val onErrorDark = Color(0xFF690005)//Ok
    val errorContainerDark = Color(0xFF93000A)//Ok
    val onErrorContainerDark = Color(0xFFFFDAD6)//Ok
    val backgroundDark = Color(0xFF161621)//OK
    val onBackgroundDark = Color(0xFFFFFFFF)//OK
    val surfaceDark = Color(0xFF0000FF)
    val onSurfaceDark = Color(0xFFFF0000)
    val surfaceVariantDark = Color(0xFF00FF00)
    val onSurfaceVariantDark = Color(0xFF0000FF)
    val outlineDark = Color(0xFFFF0000)
    val outlineVariantDark = Color(0xFF00FF00)
    val scrimDark = Color(0xFF0000FF)
    val inverseSurfaceDark = Color(0xFFFF0000)
    val inverseOnSurfaceDark = Color(0xFF00FF00)
    val inversePrimaryDark = Color(0xFF0000FF)
    val surfaceDimDark = Color(0xFFFF0000)
    val surfaceBrightDark = Color(0xFF00FF00)
    val surfaceContainerLowestDark = Color(0xFF0000FF)
    val surfaceContainerLowDark = Color(0xFFFF0000)
    val surfaceContainerDark = Color(0xFF00FF00)
    val surfaceContainerHighDark = Color(0xFF0000FF)
    val surfaceContainerHighestDark = Color(0xFFFF0000)
}


internal val fuseCSGOMatchesColorScheme = darkColorScheme(
    primary = FuseCSGOMatchesColors.primaryDark,
    onPrimary = FuseCSGOMatchesColors.onPrimaryDark,
    primaryContainer = FuseCSGOMatchesColors.primaryContainerDark,
    onPrimaryContainer = FuseCSGOMatchesColors.onPrimaryContainerDark,
    secondary = FuseCSGOMatchesColors.secondaryDark,
    onSecondary = FuseCSGOMatchesColors.onSecondaryDark,
    secondaryContainer = FuseCSGOMatchesColors.secondaryContainerDark,
    onSecondaryContainer = FuseCSGOMatchesColors.onSecondaryContainerDark,
    tertiary = FuseCSGOMatchesColors.tertiaryDark,
    onTertiary = FuseCSGOMatchesColors.onTertiaryDark,
    tertiaryContainer = FuseCSGOMatchesColors.tertiaryContainerDark,
    onTertiaryContainer = FuseCSGOMatchesColors.onTertiaryContainerDark,
    error = FuseCSGOMatchesColors.errorDark,
    onError = FuseCSGOMatchesColors.onErrorDark,
    errorContainer = FuseCSGOMatchesColors.errorContainerDark,
    onErrorContainer = FuseCSGOMatchesColors.onErrorContainerDark,
    background = FuseCSGOMatchesColors.backgroundDark,
    onBackground = FuseCSGOMatchesColors.onBackgroundDark,
    surface = FuseCSGOMatchesColors.surfaceDark,
    onSurface = FuseCSGOMatchesColors.onSurfaceDark,
    surfaceVariant = FuseCSGOMatchesColors.surfaceVariantDark,
    onSurfaceVariant = FuseCSGOMatchesColors.onSurfaceVariantDark,
    outline = FuseCSGOMatchesColors.outlineDark,
    outlineVariant = FuseCSGOMatchesColors.outlineVariantDark,
    scrim = FuseCSGOMatchesColors.scrimDark,
    inverseSurface = FuseCSGOMatchesColors.inverseSurfaceDark,
    inverseOnSurface = FuseCSGOMatchesColors.inverseOnSurfaceDark,
    inversePrimary = FuseCSGOMatchesColors.inversePrimaryDark,
    surfaceDim = FuseCSGOMatchesColors.surfaceDimDark,
    surfaceBright = FuseCSGOMatchesColors.surfaceBrightDark,
    surfaceContainerLowest = FuseCSGOMatchesColors.surfaceContainerLowestDark,
    surfaceContainerLow = FuseCSGOMatchesColors.surfaceContainerLowDark,
    surfaceContainer = FuseCSGOMatchesColors.surfaceContainerDark,
    surfaceContainerHigh = FuseCSGOMatchesColors.surfaceContainerHighDark,
    surfaceContainerHighest = FuseCSGOMatchesColors.surfaceContainerHighestDark,
)

@Composable
@ReadOnlyComposable
fun getFuseCSGOMatchesColorScheme(
    darkTheme: Boolean,
    //dynamicColor: Boolean = true
): ColorScheme {
    return when {
        //dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        //    val context = LocalContext.current
        //    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        //}

        darkTheme -> fuseCSGOMatchesColorScheme
        else -> fuseCSGOMatchesColorScheme
    }
}

val ColorScheme.transparent: Color
    @Composable
    @ReadOnlyComposable
    get() {
        return Color(0x00000000)
    }
