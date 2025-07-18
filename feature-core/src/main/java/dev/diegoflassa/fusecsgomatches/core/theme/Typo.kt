@file:Suppress("unused")

package dev.diegoflassa.fusecsgomatches.core.theme

import android.graphics.fonts.Font
import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import dev.diegoflassa.fusecsgomatches.core.R

val LocalFuseCSGOMatchesTypography = staticCompositionLocalOf { FuseCSGOMatchesTypography() }

val LocalBaselineTypography = staticCompositionLocalOf { Typography() }

class FuseCSGOMatchesTypography(
    val typography: Typography = Typography(),

    val textStyleTeam: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        color = Color.White
    ),

    val textStyleVs: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color(0x80FFFFFF)
    ),

    val textStyleNow: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp,
        lineHeight = 10.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),
)
