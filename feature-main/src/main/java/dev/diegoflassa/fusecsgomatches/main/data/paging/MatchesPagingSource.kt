package dev.diegoflassa.fusecsgomatches.main.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.diegoflassa.fusecsgomatches.core.data.enums.MatchStatus
import dev.diegoflassa.fusecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fusecsgomatches.main.data.repository.interfaces.IMatchesRepository
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class MatchesPagingSource(
    private val matchesRepository: IMatchesRepository,
) : PagingSource<Int, MatchDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchDto> {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        val currentLoadSize = params.loadSize

        return try {
            val response = matchesRepository.getMatches(
                page = pageNumber,
                pageSize = currentLoadSize
            )
            val sortedMatches: List<MatchDto> = response.body()?.sortedWith(
                compareByDescending<MatchDto> {
                    it.status == MatchStatus.IN_PROGRESS
                }.thenBy {
                    it.beginAt
                }
            ) ?: emptyList()
            val filterOpponents = response.body()?.filter {
                it.opponents?.any { opponent -> opponent?.opponent?.imageUrl != null }
                    ?: false
            }
            val filterLeague = response.body()?.filter { it.league?.imageUrl != null }
            if (filterOpponents?.isNotEmpty() ?: false) {
                Log.d("", "filterOpponents is not empty")
            } else {
                Log.d("", "filterOpponents is empty")
            }
            if (filterLeague?.isNotEmpty() ?: false) {
                Log.d("", "filterLeague is not empty")
            } else {
                Log.d("", "filterLeague is empty")
            }
            if (response.isSuccessful) {
                val nextKey = if (sortedMatches.isEmpty() || sortedMatches.size < currentLoadSize) {
                    null
                } else {
                    pageNumber + 1
                }
                val prevKey = if (pageNumber == STARTING_PAGE_INDEX) {
                    null
                } else {
                    pageNumber - 1
                }
                LoadResult.Page(
                    data = sortedMatches,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MatchDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
