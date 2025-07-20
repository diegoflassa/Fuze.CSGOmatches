package dev.diegoflassa.fusecsgomatches.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegoflassa.fusecsgomatches.core.domain.model.DomainResult
import dev.diegoflassa.fusecsgomatches.details.domain.useCases.GetOpponentsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getOpponentsUseCase: GetOpponentsUseCase, // Changed from IOpponentsRepository
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUIState())
    val uiState: StateFlow<DetailsUIState> = _uiState.asStateFlow()

    private val _effect = Channel<DetailsEffect>(Channel.BUFFERED)
    val effect: Flow<DetailsEffect> = _effect.receiveAsFlow()

    private var matchIdOrSlug: String? = savedStateHandle["matchIdOrSlug"]

    private fun loadOpponentDetails(idOrSlug: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }

            getOpponentsUseCase(idOrSlug)
                .onEach { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    opponents = result.data,
                                    error = null
                                )
                            }
                        }

                        is DomainResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.message,
                                    opponents = null
                                )
                            }
                            _effect.send(DetailsEffect.ShowError(result.message))
                        }
                    }
                }
                .launchIn(viewModelScope)
        }
    }


    fun reduce(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.NavigateToMain -> {
                viewModelScope.launch {
                    _effect.send(DetailsEffect.NavigateToMain)
                }
            }

            is DetailsIntent.Refresh -> {
                matchIdOrSlug?.let { id ->
                    if (id.isNotEmpty()) {
                        loadOpponentDetails(id)
                    } else {
                        viewModelScope.launch {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Match ID or Slug not available for Refresh."
                                )
                            }
                            _effect.send(DetailsEffect.ShowError("Match ID or Slug not available for Refresh."))
                        }
                    }
                } ?: viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Match ID or Slug not available for Refresh (is null)."
                        )
                    }
                    _effect.send(DetailsEffect.ShowError("Match ID or Slug not available for Refresh (is null)."))
                }
            }

            is DetailsIntent.LoadDetails -> {
                val currentIdOrSlug = intent.matchIdOrSlug
                savedStateHandle["matchIdOrSlug"] = currentIdOrSlug
                matchIdOrSlug = currentIdOrSlug

                if (currentIdOrSlug.isEmpty()) {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Match ID or Slug not available."
                            )
                        }
                        _effect.send(DetailsEffect.ShowError("Match ID or Slug not available for LoadDetails intent."))
                    }
                    return
                }
                loadOpponentDetails(currentIdOrSlug)
            }
        }
    }
}
