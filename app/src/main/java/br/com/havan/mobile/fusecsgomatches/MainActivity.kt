package br.com.havan.mobile.fusecsgomatches

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.padding // Not used after removing Greeting
import androidx.compose.material3.MaterialTheme
// import androidx.compose.material3.Scaffold // Not used after removing Greeting
import androidx.compose.material3.Surface
// import androidx.compose.material3.Text // Not used after removing Greeting
// import androidx.compose.runtime.Composable // Not used after removing Greeting
import androidx.compose.ui.Modifier
// import androidx.compose.ui.tooling.preview.Preview // Not used after removing Greeting
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import br.com.havan.mobile.fusecsgomatches.navigation.NavDisplay
import dev.diegoflassa.fusecsgomatches.core.navigation.NavigationViewModel
import dev.diegoflassa.fusecsgomatches.core.theme.FuseCSGOMatchesThemeContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val navigationViewModel: NavigationViewModel by viewModels()
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

        setTheme(com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar)
        enableEdgeToEdge()
        setContent {
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
        // Removed the second setContent block that was here
    }
}

// Removed Greeting Composable and its Preview as they are no longer called
// from onCreate after removing the second setContent
