package dev.diegoflassa.fusecsgomatches.details.data.repository

import dev.diegoflassa.fusecsgomatches.details.data.dto.MatchOpponentsResponseDto
import dev.diegoflassa.fusecsgomatches.details.data.network.PandaOpponentsApiService
import dev.diegoflassa.fusecsgomatches.details.data.repository.interfaces.IOpponentsRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpponentsRepository @Inject constructor(
    private val pandaOpponentsApiService: PandaOpponentsApiService
) : IOpponentsRepository {

    override suspend fun getOpponents(matchIdOrSlug: String): Response<MatchOpponentsResponseDto> {
        val ret = pandaOpponentsApiService.getOpponents(matchIdOrSlug)
        return ret
    }
}
