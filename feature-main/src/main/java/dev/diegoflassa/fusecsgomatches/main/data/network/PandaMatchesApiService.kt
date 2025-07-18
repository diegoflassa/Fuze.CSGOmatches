package dev.diegoflassa.fusecsgomatches.main.data.network

import dev.diegoflassa.fusecsgomatches.main.data.network.dto.MatchDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PandaMatchesApiService {
    @GET("matches/")
    suspend fun getMatches(
        @Query("page[number]") page: Int,
        @Query("page[size]") pageSize: Int,
        @Query("sort") sort: String = "-begin_at"
    ): Response<List<MatchDto>>
}