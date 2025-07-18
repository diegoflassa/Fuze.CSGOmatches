package dev.diegoflassa.fusecsgomatches.details.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegoflassa.fusecsgomatches.details.ui.DetailsEffect
import dev.diegoflassa.fusecsgomatches.details.ui.DetailsIntent
import dev.diegoflassa.fusecsgomatches.details.ui.DetailsUIState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUIState())
    val uiState: StateFlow<DetailsUIState> = _uiState.asStateFlow()

    private val _effect = Channel<DetailsEffect>(Channel.BUFFERED)
    val effect: Flow<DetailsEffect> = _effect.receiveAsFlow()

    fun processIntent(intent: DetailsIntent) {

    }
}
