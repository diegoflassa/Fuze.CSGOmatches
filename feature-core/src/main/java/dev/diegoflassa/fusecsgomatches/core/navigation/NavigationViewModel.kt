@file:Suppress("unused")

package dev.diegoflassa.fusecsgomatches.core.navigation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel() : ViewModel() {

    private val _state = MutableStateFlow(NavigationUIState())
    val state: StateFlow<NavigationUIState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<NavigationEffect>()
    val effect: SharedFlow<NavigationEffect> = _effect.asSharedFlow()

    /**
     * Processes the given navigation intent and updates the state accordingly.
     * This is the central logic for handling all navigation actions.
     *
     * @param intent The navigation intent to process.
     */
    fun processIntent(intent: NavigationIntent) {
        _state.update { currentState ->
            when (intent) {
                is NavigationIntent.NavigateTo -> {
                    if (currentState.backStack.lastOrNull() != intent.screen) {
                        currentState.copy(backStack = currentState.backStack + intent.screen)
                    } else {
                        viewModelScope.launch { _effect.emit(NavigationEffect.ShowToast("Already on this screen")) }
                        currentState // No change
                    }
                }

                is NavigationIntent.GoToMain -> {
                    if (currentState.backStack.lastOrNull() != Screen.Main) {
                        currentState.copy(backStack = currentState.backStack + Screen.Main)
                    } else {
                        currentState
                    }
                }

                is NavigationIntent.GoBack -> {
                    if (currentState.backStack.size > 1) {
                        currentState.copy(backStack = currentState.backStack.dropLast(1))
                    } else {
                        viewModelScope.launch {
                            _effect.emit(NavigationEffect.ShowToast("Cannot go back further"))
                        }
                        currentState
                    }
                }

                is NavigationIntent.ReplaceAll -> {
                    currentState.copy(backStack = listOf(intent.screen))
                }
            }
        }
    }

    /**
     * Navigates to a new screen by sending a [NavigationIntent.NavigateTo].
     * Adds the screen to the top of the back stack.
     *
     * @param screen The destination screen to navigate to.
     */
    fun navigateTo(screen: Screen) {
        processIntent(NavigationIntent.NavigateTo(screen))
    }

    /**
     * Navigates to the Categories screen.
     */
    fun navigateToMain(comicPath: Uri) {
        processIntent(NavigationIntent.NavigateTo(Screen.Main))
    }

    /**
     * Navigates to the Categories screen.
     */
    fun navigateToDetails() {
        processIntent(NavigationIntent.NavigateTo(Screen.Details))
    }

    /**
     * Goes back to the previous screen by sending a [NavigationIntent.GoBack].
     * Removes the current screen from the top of the back stack.
     * If already at the root, an effect is emitted.
     */
    fun goBack() {
        processIntent(NavigationIntent.GoBack)
    }

    /**
     * Clears the entire back stack and navigates to the given screen
     * by sending a [NavigationIntent.ReplaceAll].
     *
     * @param screen The screen to become the new single entry in the back stack.
     */
    fun replaceAll(screen: Screen) {
        processIntent(NavigationIntent.ReplaceAll(screen))
    }
}
