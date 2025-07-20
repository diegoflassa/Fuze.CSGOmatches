package dev.diegoflassa.fusecsgomatches.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalFuseCSGOMatchesDimen = staticCompositionLocalOf { FuseCSGOMatchesDimen() }

data class FuseCSGOMatchesDimen(
    //Common paddings
    val noPadding: Dp = 0.dp,
    val extraSmallPadding: Dp = 4.dp,
    val smallPadding: Dp = 8.dp,
    val smallMediumPadding: Dp = 12.dp,
    val mediumPadding: Dp = 16.dp,
    val mediumLargePadding: Dp = 24.dp,
    val largePadding: Dp = 32.dp,
    val bigLargePadding: Dp = 48.dp,
    val extraLargePadding: Dp = 64.dp,
    val extraBigLargePadding: Dp = 76.dp,

    //Specific paddings
    val mainTopAppBarHeight: Dp = 40.dp,
    val mainTopAppContentTopPadding: Dp = 24.dp,
    val mainTopAppContentEndPadding: Dp = 18.dp,
    val horizontalCardsPaddingDetails: Dp = 13.dp,
    val verticalCardsPaddingDetails: Dp = 12.dp,
    val badgeHeight: Dp = 25.dp,
    val teamImageVsPadding: Dp = 20.dp,
    val mainMatchDateTeamInfoPadding: Dp = 20.dp,
    val detailsTopAppBarHeight: Dp = 80.dp,
    val detailsMatchBoxSize: Dp = 82.dp,
    val detailsNicknameTopPadding: Dp = 15.dp,
    val detailsPlayerImageSize: Dp = (48.5).dp,
    val detailsPlayerImageHorizontalPadding: Dp = (11.76).dp,
)
