package dev.diegoflassa.fusecsgomatches.main.domain.useCases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.diegoflassa.fusecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fusecsgomatches.main.data.paging.MatchesPagingSource
import dev.diegoflassa.fusecsgomatches.main.data.repository.interfaces.IMatchesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val matchesRepository: IMatchesRepository
) : IGetMatchesUseCase {
    override operator fun invoke(
        pagingConfig: PagingConfig,
        onlyFutureGames: Boolean,
        selectedGames: Set<String>,
        onGamesDiscovered: ((games: Set<String>) -> Unit)?
    ): Flow<PagingData<MatchDto>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                MatchesPagingSource(
                    matchesRepository = matchesRepository,
                    onlyFutureGames = onlyFutureGames,
                    selectedGames = selectedGames,
                    onGamesDiscovered = onGamesDiscovered
                )
            }
        ).flow
    }
}