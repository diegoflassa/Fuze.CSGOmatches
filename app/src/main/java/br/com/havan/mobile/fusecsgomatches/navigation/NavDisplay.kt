package br.com.havan.mobile.fusecsgomatches.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entry // Ensure this is the correct import for navigation3
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import dev.diegoflassa.fusecsgomatches.ui.DetailsScreen
import dev.diegoflassa.fusecsgomatches.ui.MainScreen
import dev.diegoflassa.fusecsgomatches.core.navigation.NavigationViewModel
import dev.diegoflassa.fusecsgomatches.core.navigation.Screen

@Composable
fun NavDisplay(modifier: Modifier, navigationViewModel: NavigationViewModel) {
    val backstack = navigationViewModel.state.collectAsStateWithLifecycle().value.backStack
    NavDisplay(
        backStack = backstack,
        modifier = modifier,
        transitionSpec = {
            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
        },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Screen.Main> {
                MainScreen(navigationViewModel = navigationViewModel)
            }
            entry<Screen.Details> {
                DetailsScreen(navigationViewModel = navigationViewModel)
            }
        }
    )
}

