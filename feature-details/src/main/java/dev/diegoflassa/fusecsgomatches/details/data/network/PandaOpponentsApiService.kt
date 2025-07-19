package dev.diegoflassa.fusecsgomatches.details.data.network

import dev.diegoflassa.fusecsgomatches.details.data.dto.OpponentsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PandaOpponentsApiService {
    @GET("matches/{match_id_or_slug}/opponents")
    suspend fun getOpponents(
        @Path("match_id_or_slug") matchIdOrSlug: String
    ): Response<OpponentsResponseDto>
}
