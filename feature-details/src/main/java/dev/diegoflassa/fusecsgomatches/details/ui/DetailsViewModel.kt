package dev.diegoflassa.fusecsgomatches.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegoflassa.fusecsgomatches.details.data.dto.OpponentsResponseDto
import dev.diegoflassa.fusecsgomatches.details.data.repository.interfaces.IOpponentsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val opponentsRepository: IOpponentsRepository,
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

            try {
                val response: Response<OpponentsResponseDto> =
                    opponentsRepository.getOpponents(idOrSlug)

                if (response.isSuccessful) {
                    val opponentsData = response.body()
                    if (opponentsData != null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                opponents = opponentsData,
                                error = null
                            )
                        }
                    } else {
                        val errorMessage = "Empty response body from server."
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = errorMessage,
                                opponents = null
                            )
                        }
                        _effect.send(DetailsEffect.ShowError(errorMessage))
                    }
                } else {
                    val errorMessage = "API Error: ${response.code()} - ${
                        response.message().ifEmpty { "Unknown API error" }
                    }"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage,
                            opponents = null
                        )
                    }
                    _effect.send(DetailsEffect.ShowError(errorMessage))
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An unexpected error occurred."
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = errorMessage,
                        opponents = null
                    )
                }
                _effect.send(DetailsEffect.ShowError(errorMessage))
            }
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

