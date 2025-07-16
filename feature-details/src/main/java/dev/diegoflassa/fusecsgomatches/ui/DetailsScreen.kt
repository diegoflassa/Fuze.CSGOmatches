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

private const val tag = "DetailsScreen"

@Composable
fun DetailsScreen(
    navigationViewModel: NavigationViewModel = hiltActivityViewModel(),
    detailsViewModel: DetailsViewModel = DetailsViewModel(),
) {
    val uiState by detailsViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        detailsViewModel.processIntent(DetailsIntent.Placeholder)
    }

    LaunchedEffect(key1 = detailsViewModel.effect) {
        detailsViewModel.effect.collectLatest { effect ->
            when (effect) {
                is DetailsEffect.Placeholder -> {
                }
            }
        }
    }

    DetailsScreenContent(
        navigationViewModel = navigationViewModel,
        uiState = uiState,
        onIntent = detailsViewModel::processIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenContent(
    modifier: Modifier = Modifier,
    navigationViewModel: NavigationViewModel? = null,
    uiState: DetailsUIState,
    onIntent: ((DetailsIntent) -> Unit)? = null,
) {
}
