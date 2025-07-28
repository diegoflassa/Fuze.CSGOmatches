package dev.diegoflassa.fuzecsgomatches

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dev.diegoflassa.fuzecsgomatches.navigation.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import dev.diegoflassa.fuzecsgomatches.core.extensions.hiltActivityViewModel
import dev.diegoflassa.fuzecsgomatches.core.navigation.NavigationViewModel
import dev.diegoflassa.fuzecsgomatches.core.navigation.Screen
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesThemeContent
import dev.diegoflassa.fuzecsgomatches.core.ui.DialogManager
import dev.diegoflassa.fuzecsgomatches.core.ui.DialogManagerFactory
import dev.diegoflassa.fuzecsgomatches.core.ui.desejaSairDoAppDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val tag = "MainActivity"

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

        setTheme(com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar)
        enableEdgeToEdge()
        setContent {
            var dialogManager by remember { mutableStateOf<DialogManager?>(null) }
            val navigationViewModel: NavigationViewModel = hiltActivityViewModel()

            FuzeCSGOMatchesThemeContent {
                LaunchedEffect(Unit) {
                    dialogManager = DialogManagerFactory.getDialogManager(tag)
                }
                val dialogsParaExibir = dialogManager?.dialogsParaExibir?.collectAsStateWithLifecycle()

                if (dialogsParaExibir?.value?.isNotEmpty() == true) {
                    dialogManager?.dialogAtual()?.ExibirDialog()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavDisplay(modifier = Modifier, navigationViewModel = navigationViewModel)
                }
            }

            BackHandler {
                handleBackPress(
                    navigationViewModel,
                    dialogManager
                )
            }
        }
    }

    private fun handleBackPress(
        navigationViewModel: NavigationViewModel,
        dialogManager: DialogManager?
    ) {
        if (navigationViewModel.state.value.backStack.lastOrNull() == Screen.Main) {
            val dialog = desejaSairDoAppDialog(
                title = getString(R.string.attention),
                text = getString(R.string.texto_dialog_deseja_sair_do_app),
                onDismiss = {
                    dialogManager?.removerDialog()
                }) {
                dialogManager?.removerDialog()
                finish()
            }
            dialogManager?.adicionarDialog(dialog)
        } else {
            navigationViewModel.goBack()
        }
    }
}
