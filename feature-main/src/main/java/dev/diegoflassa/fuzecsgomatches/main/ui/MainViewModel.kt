package dev.diegoflassa.fuzecsgomatches.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegoflassa.fuzecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fuzecsgomatches.main.domain.useCases.GetMatchesUseCase
import dev.diegoflassa.fuzecsgomatches.main.ui.MainEffect.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
) : ViewModel() {

    private val pagingConfig =
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 40,
            prefetchDistance = 15
        )

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MainEffect>()
    val effect = _effect.receiveAsFlow()

    private val _matchesFlow: MutableStateFlow<PagingData<MatchDto>> = MutableStateFlow(PagingData.empty())
    val matchesFlow = _matchesFlow.asStateFlow()

    fun reduce(intent: MainIntent) {
        viewModelScope.launch {
            when (intent) {
                is MainIntent.LoadMatches -> {
                    fetchMatches()
                }

                is MainIntent.ShowFilter -> {
                    _effect.send(
                        ShowFilter
                    )
                }

                is MainIntent.ApplyFilter -> {
                    _uiState.update {
                        uiState.value.copy(
                            onlyFutureGames = intent.onlyFutureEvents
                        )
                    }
                    fetchMatches()
                }

                is MainIntent.OnMatchClicked -> {
                    _effect.send(
                        NavigateToDetails(
                            intent.matchIdOrSlug,
                            intent.leagueName,
                            intent.serieFullName,
                            intent.scheduledAt
                        )
                    )
                }

            }
        }
    }

    private fun fetchMatches() {
        _matchesFlow.value = PagingData.empty()
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }

        viewModelScope.launch {
            try {
                getMatchesUseCase(
                    pagingConfig = pagingConfig,
                    onlyFutureGames = uiState.value.onlyFutureGames,
                )
                .cachedIn(viewModelScope)
                .collectLatest { newPagingData ->
                    _matchesFlow.value = newPagingData
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        error = e.localizedMessage ?: "Failed to load matches",
                        isLoading = false
                    )
                }
            }
        }
    }
}
