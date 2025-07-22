package dev.diegoflassa.fuzecsgomatches.main.data.network

import dev.diegoflassa.fuzecsgomatches.main.data.dto.MatchDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PandaMatchesApiService {
    @GET("matches/")
    suspend fun getMatches(
        @Query("page[number]") page: Int,
        @Query("page[size]") pageSize: Int,
        @Query("search[name]") searchName: String = "strike",
        @Query("filter[begin_at]") beginAt: String = "",
        @Query("sort") sort: String = "-begin_at"
    ): Response<List<MatchDto>>
}