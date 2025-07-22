package dev.diegoflassa.fuzecsgomatches.main.data.repository

import dev.diegoflassa.fuzecsgomatches.main.data.network.PandaMatchesApiService
import dev.diegoflassa.fuzecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fuzecsgomatches.main.data.repository.interfaces.IMatchesRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchesRepository @Inject constructor(
    private val pandaMatchesApiService: PandaMatchesApiService
) : IMatchesRepository {

    override suspend fun getMatches(
        page: Int,
        pageSize: Int,
        beginAt: String
    ): Response<List<MatchDto>> {
        val ret =
            pandaMatchesApiService.getMatches(page = page, pageSize = pageSize, beginAt = beginAt)
        return ret
    }
}
