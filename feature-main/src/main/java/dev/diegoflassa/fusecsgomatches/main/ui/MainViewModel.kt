package dev.diegoflassa.fusecsgomatches.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegoflassa.fusecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fusecsgomatches.main.data.paging.MatchesPagingSource
import dev.diegoflassa.fusecsgomatches.main.data.repository.interfaces.IMatchesRepository
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
    private val matchesRepository: IMatchesRepository,
) : ViewModel() {

    //pageSize common value is 20
    private val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false)

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MainEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        // Load initial matches when ViewModel is created
        fetchMatches()
    }

    fun reduce(intent: MainIntent) {
        viewModelScope.launch {
            when (intent) {
                is MainIntent.LoadMatches -> {
                    // This intent might become redundant if we load on init and Paging handles refreshes.
                    // Or it could trigger a refresh.
                    fetchMatches()
                }
                is MainIntent.RefreshMatches -> {
                    fetchMatches() // This will now create a new Flow from Pager
                }
                is MainIntent.OnMatchClicked -> {
                    _effect.send(MainEffect.NavigateToDetails(intent.matchIdOrSlug))
                }
            }
        }
    }

    private fun fetchMatches() {
        // isLoading and error can be handled by observing Paging's LoadState
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            val newMatchesFlow: Flow<PagingData<MatchDto>> = Pager(
                config = pagingConfig,
                pagingSourceFactory = { MatchesPagingSource(matchesRepository) }
            ).flow.cachedIn(viewModelScope)

            _uiState.update {
                it.copy(
                    matchesFlow = newMatchesFlow,
                    isLoading = false // Paging's LoadState will reflect loading
                )
            }
        } catch (e: Exception) {
             _uiState.update {
                it.copy(
                    error = e.localizedMessage ?: "Failed to load matches",
                    isLoading = false
                )
            }
            // Consider sending an effect for the error as previously discussed
            // viewModelScope.launch { _effect.send(MainEffect.ShowError(e.localizedMessage ?: "Failed to load matches")) }
        }
    }
}
