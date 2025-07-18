package dev.diegoflassa.fusecsgomatches.main.data.repository

import dev.diegoflassa.fusecsgomatches.main.data.network.PandaMatchesApiService
import dev.diegoflassa.fusecsgomatches.main.data.network.dto.MatchDto
import dev.diegoflassa.fusecsgomatches.main.data.repository.interfaces.IMatchesRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchesRepository @Inject constructor(
    private val pandaMatchesApiService: PandaMatchesApiService
) : IMatchesRepository {

    override suspend fun getMatches(
        page: Int,
        pageSize: Int
    ): Response<List<MatchDto>> {
        val ret = pandaMatchesApiService.getMatches(page, pageSize)
        return ret
    }
}
