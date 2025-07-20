package dev.diegoflassa.fusecsgomatches.main.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.diegoflassa.fusecsgomatches.core.data.enums.MatchStatus
import dev.diegoflassa.fusecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fusecsgomatches.main.data.repository.interfaces.IMatchesRepository
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MatchesPagingSource(
    private val matchesRepository: IMatchesRepository,
    private val onlyFutureGames: Boolean = true,
) : PagingSource<Int, MatchDto>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MatchDto> {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        val currentLoadSize = params.loadSize

        return try {
            val beginAt = if (onlyFutureGames) {
                getCurrentDateAsString()
            } else {
                ""
            }

            val response = matchesRepository.getMatches(
                page = pageNumber,
                pageSize = currentLoadSize,
                beginAt = beginAt
            )

            val allMatchesFromApi = response.body() ?: emptyList()

            val finalSortedMatches = allMatchesFromApi.sortedWith(
                compareBy<MatchDto> { match ->
                    when (match.status) {
                        MatchStatus.IN_PROGRESS -> 0
                        MatchStatus.SCHEDULED -> 1
                        MatchStatus.FINISHED -> 2
                        MatchStatus.CANCELED -> 3
                        MatchStatus.UNKNOWN, null -> 4
                        else -> 5
                    }
                }.thenBy { match ->
                    match.beginAt ?: match.scheduledAt ?: Instant.MAX
                }
            )

            if (response.isSuccessful) {
                val isLastPageFromAPI = allMatchesFromApi.size < currentLoadSize

                val nextKey = if (isLastPageFromAPI) {
                    null
                } else {
                    pageNumber + 1
                }

                val prevKey = if (pageNumber == STARTING_PAGE_INDEX) {
                    null
                } else {
                    pageNumber - 1
                }

                Log.d("MatchesPagingSource", "Run")

                LoadResult.Page(
                    data = finalSortedMatches,
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
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun getCurrentDateAsString(): String {
        val currentDate: LocalDate = LocalDate.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    override fun getRefreshKey(state: PagingState<Int, MatchDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}