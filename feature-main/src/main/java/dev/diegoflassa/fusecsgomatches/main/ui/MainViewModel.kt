package dev.diegoflassa.fusecsgomatches.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegoflassa.fusecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fusecsgomatches.main.domain.useCases.GetMatchesUseCase
import dev.diegoflassa.fusecsgomatches.main.domain.useCases.IGetMatchesUseCase
import dev.diegoflassa.fusecsgomatches.main.ui.MainEffect.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: IGetMatchesUseCase,
) : ViewModel(), (Set<String>) -> Unit {

    private val pagingConfig =
        PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 40, prefetchDistance = 20)

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MainEffect>()
    val effect = _effect.receiveAsFlow()

    private var filterGames: Set<String> = setOf()

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
                    filterGames = intent.selectedGames
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
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            val newMatchesFlow: Flow<PagingData<MatchDto>> =
                getMatchesUseCase(
                    pagingConfig = pagingConfig,
                    onlyFutureGames = uiState.value.onlyFutureGames,
                    selectedGames = filterGames,
                    onGamesDiscovered = this@MainViewModel
                ).cachedIn(viewModelScope)

            _uiState.update {
                it.copy(
                    matchesFlow = newMatchesFlow,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    error = e.localizedMessage ?: "Failed to load matches",
                    isLoading = false
                )
            }
        }
    }

    override fun invoke(discoveredGameNames: Set<String>) {
        _uiState.update { currentState ->
            if (currentState.games.isEmpty()) {
                val initialGamePairs = discoveredGameNames.map { gameName ->
                    val isSelected = gameName.lowercase().let { lowercasedName ->
                        lowercasedName.contains("counter") ||
                                lowercasedName.contains("strike") ||
                                lowercasedName.contains("cs")
                    }
                    Pair(gameName, isSelected)
                }
                if (initialGamePairs.isNotEmpty()) {
                    currentState.copy(games = initialGamePairs)
                } else {
                    currentState
                }
            } else {
                val currentGamesList = currentState.games
                val existingGameNames = currentGamesList.map { it.first }.toSet()

                val newGamePairs = discoveredGameNames
                    .filterNot { discoveredName -> existingGameNames.contains(discoveredName) }
                    .map { newName -> Pair(newName, false) }

                if (newGamePairs.isNotEmpty()) {
                    currentState.copy(games = currentGamesList + newGamePairs)
                } else {
                    currentState
                }
            }
        }
    }
}
