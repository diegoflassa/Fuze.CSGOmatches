package dev.diegoflassa.fusecsgomatches.details.data.repository.interfaces

import dev.diegoflassa.fusecsgomatches.details.data.dto.MatchOpponentsResponseDto
import retrofit2.Response

/**
 * Repository interface for operations related to PandaMatchesApiService.
 */
interface IOpponentsRepository {

    suspend fun getOpponents(
        matchIdOrSlug: String
    ): Response<MatchOpponentsResponseDto>
}