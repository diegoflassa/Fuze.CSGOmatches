package dev.diegoflassa.fuzecsgomatches.main.domain.useCases

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.diegoflassa.fuzecsgomatches.main.data.dto.MatchDto
import kotlinx.coroutines.flow.Flow

interface IGetMatchesUseCase {
    operator fun invoke(
        pagingConfig: PagingConfig,
        onlyFutureGames: Boolean = true
    ): Flow<PagingData<MatchDto>>
}
