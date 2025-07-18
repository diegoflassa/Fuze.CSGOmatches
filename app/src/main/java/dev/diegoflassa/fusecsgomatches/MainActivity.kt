package dev.diegoflassa.fusecsgomatches

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dev.diegoflassa.fusecsgomatches.navigation.NavDisplay
import com.google.android.material.R
import dagger.hilt.android.AndroidEntryPoint
import dev.diegoflassa.fusecsgomatches.core.extensions.hiltActivityViewModel
import dev.diegoflassa.fusecsgomatches.core.navigation.NavigationViewModel
import dev.diegoflassa.fusecsgomatches.core.theme.FuseCSGOMatchesThemeContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var keepSplashOnScreen = true
    private val splashScreenDisplayTime = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        lifecycleScope.launch {
            delay(splashScreenDisplayTime)
            keepSplashOnScreen = false
        }

        setTheme(R.style.Theme_Material3_DayNight_NoActionBar)
        enableEdgeToEdge()
        setContent {
            val navigationViewModel: NavigationViewModel = hiltActivityViewModel()
            //LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            FuseCSGOMatchesThemeContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavDisplay(modifier = Modifier, navigationViewModel = navigationViewModel)
                }
            }

            BackHandler {
                navigationViewModel.goBack()
            }
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}
