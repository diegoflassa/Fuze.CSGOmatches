package dev.diegoflassa.fuzecsgomatches.main.domain.useCases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.diegoflassa.fuzecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fuzecsgomatches.main.data.paging.MatchesPagingSource
import dev.diegoflassa.fuzecsgomatches.main.data.repository.interfaces.IMatchesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val matchesRepository: IMatchesRepository
) : IGetMatchesUseCase {
    override operator fun invoke(
        pagingConfig: PagingConfig,
        onlyFutureGames: Boolean,
    ): Flow<PagingData<MatchDto>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                MatchesPagingSource(
                    matchesRepository = matchesRepository,
                    onlyFutureGames = onlyFutureGames,
                )
            }
        ).flow
    }
}