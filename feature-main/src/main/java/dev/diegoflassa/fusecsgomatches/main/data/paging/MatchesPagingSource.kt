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
    private var onlyFutureGames: Boolean = true,
    private var selectedGames: Set<String> = setOf(),
    private var onGamesDiscovered: ((games: Set<String>) -> Unit)? = null
) : PagingSource<Int, MatchDto>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MatchDto> {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        val currentLoadSize = params.loadSize

        return try {
            val response = matchesRepository.getMatches(
                page = pageNumber,
                pageSize = currentLoadSize
            )

            val allMatchesOnPage = response.body() ?: emptyList()
            val gamesDiscoveredOnThisPage: MutableSet<String> = mutableSetOf()
            val currentMoment = Instant.now()

            val matchesAfterGameFilter = allMatchesOnPage.filter { match ->
                val videogameTitleName =
                    match.videogameTitle?.name?.lowercase(Locale.getDefault())
                gamesDiscoveredOnThisPage.add(videogameTitleName ?: "N/A")

                if (selectedGames.isNotEmpty()) {
                    if (selectedGames.contains("N/A")) {
                        selectedGames.contains(videogameTitleName) || (videogameTitleName?.isEmpty() == true)
                    } else {
                        selectedGames.contains(videogameTitleName)
                    }
                } else {
                    true
                }
            }

            val matchesAfterFutureFilter = matchesAfterGameFilter.filter { match ->
                if (onlyFutureGames) {
                    val matchTime = match.beginAt ?: match.scheduledAt
                    matchTime != null && !matchTime.isBefore(currentMoment)
                } else {
                    true
                }
            }

            val finalSortedMatches = matchesAfterFutureFilter.sortedWith(
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

            if (gamesDiscoveredOnThisPage.isNotEmpty()) {
                onGamesDiscovered?.invoke(gamesDiscoveredOnThisPage)
            }

            if (response.isSuccessful) {
                val isLastPageFromAPI = allMatchesOnPage.size < currentLoadSize

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
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MatchDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
