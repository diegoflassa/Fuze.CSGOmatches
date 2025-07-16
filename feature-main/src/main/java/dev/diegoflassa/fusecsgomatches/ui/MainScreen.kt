package dev.diegoflassa.fusecsgomatches.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.diegoflassa.fusecsgomatches.core.extensions.hiltActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import dev.diegoflassa.fusecsgomatches.core.navigation.NavigationViewModel

private const val tag = "MainScreen"

@Composable
fun MainScreen(
    navigationViewModel: NavigationViewModel = hiltActivityViewModel(),
    mainViewModel: MainViewModel = MainViewModel(),
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        mainViewModel.processIntent(MainIntent.Placeholder)
    }

    LaunchedEffect(key1 = mainViewModel.effect) {
        mainViewModel.effect.collectLatest { effect ->
            when (effect) {
                is MainEffect.Placeholder -> {
                }
            }
        }
    }

    MainScreenContent(
        navigationViewModel = navigationViewModel,
        uiState = uiState,
        onIntent = mainViewModel::processIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    navigationViewModel: NavigationViewModel? = null,
    uiState: MainUIState,
    onIntent: ((MainIntent) -> Unit)? = null,
) {
}
