package dev.diegoflassa.fuzecsgomatches.details.data.repository

import dev.diegoflassa.fuzecsgomatches.details.data.dto.OpponentsResponseDto
import dev.diegoflassa.fuzecsgomatches.details.data.network.PandaOpponentsApiService
import dev.diegoflassa.fuzecsgomatches.details.data.repository.interfaces.IOpponentsRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpponentsRepository @Inject constructor(
    private val pandaOpponentsApiService: PandaOpponentsApiService
) : IOpponentsRepository {

    override suspend fun getOpponents(matchIdOrSlug: String): Response<OpponentsResponseDto> {
        val ret = pandaOpponentsApiService.getOpponents(matchIdOrSlug)
        return ret
    }
}
