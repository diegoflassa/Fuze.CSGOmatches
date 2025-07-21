package dev.diegoflassa.fusecsgomatches.details.domain.useCases

import dev.diegoflassa.fusecsgomatches.core.domain.model.DomainResult
import dev.diegoflassa.fusecsgomatches.details.data.dto.OpponentsResponseDto
import kotlinx.coroutines.flow.Flow

interface IGetOpponentsUseCase {
    operator fun invoke(idOrSlug: String): Flow<DomainResult<OpponentsResponseDto>>
}
