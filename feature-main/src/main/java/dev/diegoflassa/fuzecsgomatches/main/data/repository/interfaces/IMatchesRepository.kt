package dev.diegoflassa.fuzecsgomatches.main.data.repository.interfaces

import dev.diegoflassa.fuzecsgomatches.main.data.dto.MatchDto
import retrofit2.Response

interface IMatchesRepository {

    suspend fun getMatches(
        page: Int,
        pageSize: Int,
        beginAt: String
    ): Response<List<MatchDto>>
}