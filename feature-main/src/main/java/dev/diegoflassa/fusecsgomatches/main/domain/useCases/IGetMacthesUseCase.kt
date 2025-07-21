package dev.diegoflassa.fusecsgomatches.main.domain.useCases

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.diegoflassa.fusecsgomatches.main.data.dto.MatchDto
import kotlinx.coroutines.flow.Flow

interface IGetMatchesUseCase {
    operator fun invoke(
        pagingConfig: PagingConfig,
        onlyFutureGames: Boolean = true
    ): Flow<PagingData<MatchDto>>
}
