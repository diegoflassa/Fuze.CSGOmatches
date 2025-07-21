package dev.diegoflassa.fusecsgomatches.main.data.repository.interfaces

import dev.diegoflassa.fusecsgomatches.main.data.dto.MatchDto
import retrofit2.Response

interface IMatchesRepository {

    suspend fun getMatches(
        page: Int,
        pageSize: Int,
        beginAt: String
    ): Response<List<MatchDto>>
}