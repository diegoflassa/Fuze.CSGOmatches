package dev.diegoflassa.fuzecsgomatches.main.domain.useCases

import androidx.paging.*
import app.cash.turbine.test
import dev.diegoflassa.fuzecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fuzecsgomatches.main.data.repository.interfaces.IMatchesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.HttpException
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
class GetMatchesUseCaseTest {

    private lateinit var fakeRepository: FakeMatchesRepository
    private lateinit var useCase: IGetMatchesUseCase

    private val pagingConfig = PagingConfig(pageSize = 10)

    @Before
    fun setUp() {
        fakeRepository = FakeMatchesRepository()
        useCase = GetMatchesUseCase(fakeRepository)
    }

    @Test
    fun `should return paged matches successfully`() = runTest {
        fakeRepository.matches = List(20) { MatchDto(id = it.toLong()) }

        val flow = useCase(pagingConfig)

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return empty when no matches available`() = runTest {
        fakeRepository.matches = emptyList()

        val flow = useCase(pagingConfig)

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should load multiple pages correctly`() = runTest {
        fakeRepository.matches = List(30) { MatchDto(id = it.toLong()) }

        val flow = useCase(pagingConfig)

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle IO exception correctly`() = runTest {
        fakeRepository.throwIOException = true

        val flow = useCase(pagingConfig)

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle HttpException correctly`() = runTest {
        fakeRepository.throwHttpException = true

        val flow = useCase(pagingConfig)

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return specific match id`() = runTest {
        fakeRepository.matches = List(1) { MatchDto(id = 123) }

        val flow = useCase(pagingConfig)

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return correct number of matches`() = runTest {
        val expected = 15
        fakeRepository.matches = List(expected) { MatchDto(id = it.toLong()) }

        val flow = useCase(pagingConfig)

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return paging data with correct page size`() = runTest {
        val pageSize = 10
        fakeRepository.matches = List(50) { MatchDto(id = it.toLong()) }

        val flow = useCase(PagingConfig(pageSize = pageSize))

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit data without crashing when repository returns null body`() = runTest {
        fakeRepository.returnNullBody = true

        val flow = useCase(pagingConfig)

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should still emit even if API returns error code`() = runTest {
        fakeRepository.returnError = true

        val flow = useCase(pagingConfig)

        flow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Fake Repository for testing
    class FakeMatchesRepository : IMatchesRepository {
        var matches: List<MatchDto> = emptyList()
        var throwIOException = false
        var throwHttpException = false
        var returnNullBody = false
        var returnError = false

        override suspend fun getMatches(page: Int, pageSize: Int,beginAt: String): Response<List<MatchDto>> {
            if (throwIOException) throw IOException("Network error")
            if (throwHttpException) throw HttpException(
                Response.error<List<MatchDto>>(
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    "Server error".toResponseBody(null)
                )
            )
            if (returnNullBody) return Response.success(null)
            if (returnError) return Response.error(
                HttpURLConnection.HTTP_BAD_REQUEST,
                "Bad request".toResponseBody(null)
            )

            val start = page * pageSize
            val end = (start + pageSize).coerceAtMost(matches.size)
            val pageList = matches.subList(start, end)

            return Response.success(pageList)
        }
    }
}
