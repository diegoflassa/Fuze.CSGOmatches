package dev.diegoflassa.fusecsgomatches.main.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.diegoflassa.fusecsgomatches.core.data.enums.MatchStatus
import dev.diegoflassa.fusecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fusecsgomatches.main.data.repository.interfaces.IMatchesRepository
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.util.Locale

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
            val currentMoment = Instant.now()
            val sortedMatches: List<MatchDto> = response.body()?.filter { match ->
                val matchTime = match.beginAt ?: match.scheduledAt
                matchTime != null && matchTime.isBefore(currentMoment).not()
            }?.filter { match ->
                val videogameTitleName = match.videogameTitle?.name?.lowercase(Locale.getDefault())
                videogameTitleName != null && (
                        videogameTitleName.contains("counter") ||
                                videogameTitleName.contains("strike") ||
                                videogameTitleName.contains("cs")
                        )
            }?.sortedWith(
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
            ) ?: emptyList()
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
