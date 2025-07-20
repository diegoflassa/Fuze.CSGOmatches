package dev.diegoflassa.fusecsgomatches.details.data.repository.interfaces

import dev.diegoflassa.fusecsgomatches.details.data.dto.OpponentsResponseDto
import retrofit2.Response

interface IOpponentsRepository {

    suspend fun getOpponents(
        matchIdOrSlug: String
    ): Response<OpponentsResponseDto>
}