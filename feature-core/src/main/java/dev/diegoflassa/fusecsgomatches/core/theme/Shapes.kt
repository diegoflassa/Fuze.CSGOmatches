package dev.diegoflassa.fusecsgomatches.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

val LocalFuseCSGOMatchesShapes = staticCompositionLocalOf { FuseCSGOMatchesShapes() }
val fuseCSGOMatchesDimen = FuseCSGOMatchesDimen()

data class FuseCSGOMatchesShapes(
    val agora: Shape = AgoraTagShape(),
)

class AgoraTagShape(
    private val bottomLeftCornerRadiusDp: Dp = fuseCSGOMatchesDimen.smallPadding
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            val bottomLeftRadiusPx = with(density) { bottomLeftCornerRadiusDp.toPx() }

            moveTo(0f, 0f)

            lineTo(size.width, 0f)

            lineTo(size.width, size.height)

            lineTo(bottomLeftRadiusPx, size.height)
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = size.height - bottomLeftRadiusPx * 2,
                    right = bottomLeftRadiusPx * 2,
                    bottom = size.height
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        })
    }
}
