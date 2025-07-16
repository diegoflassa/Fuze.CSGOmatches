package dev.diegoflassa.fusecsgomatches.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

open class DetailsViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUIState())
    val uiState: StateFlow<DetailsUIState> = _uiState.asStateFlow()

    private val _effect = Channel<DetailsEffect>(Channel.BUFFERED)
    val effect: Flow<DetailsEffect> = _effect.receiveAsFlow()

    open fun processIntent(intent: DetailsIntent) {

    }
}
