@file:Suppress("unused")

package dev.diegoflassa.fuzecsgomatches.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val LocalFuzeCSGOMatchesTypography = staticCompositionLocalOf { FuzeCSGOMatchesTypography() }

val LocalBaselineTypography = staticCompositionLocalOf { Typography() }

class FuzeCSGOMatchesTypography(
    val typography: Typography = Typography(),

    val textStyleNoMatchesFound: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    val textStyleTeam: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        color = Color.White
    ),

    val textStyleVs: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = FuzeCSGOMatchesColors.vsTextColor
    ),

    val textStyleMainMatchDate: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    val textStyleNow: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    val textStyleEnded: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    val textStyleCancelled: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),

    val textStyleLeagueAndSeries: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),
    val textStyleMainScreenTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Start,
        color = Color.White
    ),
    val textStyleMainScreenTeamName: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White,
    ),
    val textStyleDetailsScreenTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),
    val textStyleDetailsCardNickname: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    ),
    val textStyleDetailsCardName: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = TextUnit.Unspecified,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = FuzeCSGOMatchesColors.colorDetailsCardName
    ),
)
