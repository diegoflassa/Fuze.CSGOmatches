package dev.diegoflassa.fuzecsgomatches.details.domain.useCases

import dev.diegoflassa.fuzecsgomatches.core.domain.model.DomainResult
import dev.diegoflassa.fuzecsgomatches.details.data.dto.OpponentsResponseDto
import kotlinx.coroutines.flow.Flow

interface IGetOpponentsUseCase {
    operator fun invoke(idOrSlug: String): Flow<DomainResult<OpponentsResponseDto>>
}
