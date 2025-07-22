package dev.diegoflassa.fuzecsgomatches.core.ui

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.widget.ProgressBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesTheme

@Composable
fun SystemCircularLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = FuzeCSGOMatchesTheme.colorScheme.tertiary
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { context: Context ->
                ProgressBar(context, null, android.R.attr.progressBarStyle).apply {
                }
            },
            update = { progressBar: ProgressBar ->
                val androidColor = color.toArgb()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    progressBar.indeterminateDrawable?.colorFilter =
                        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                            androidColor,
                            BlendModeCompat.SRC_IN
                        )
                } else {
                    @Suppress("DEPRECATION")
                    progressBar.indeterminateDrawable?.setColorFilter(
                        androidColor,
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }
        )
    }
}
