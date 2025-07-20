package dev.diegoflassa.fusecsgomatches.details.domain.useCases

import android.net.Uri
import androidx.core.net.toUri
import app.cash.turbine.test
import dev.diegoflassa.fusecsgomatches.core.domain.model.DomainResult
import dev.diegoflassa.fusecsgomatches.details.data.dto.OpponentTeamDetailDto
import dev.diegoflassa.fusecsgomatches.details.data.dto.OpponentsResponseDto
import dev.diegoflassa.fusecsgomatches.details.data.repository.interfaces.IOpponentsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.time.Instant

@ExperimentalCoroutinesApi
class GetOpponentUseCaseTest {

    private lateinit var opponentsRepository: IOpponentsRepository
    private lateinit var getOpponentsUseCase: GetOpponentsUseCase
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockUri: Uri

    @Before
    fun setUp() {
        opponentsRepository = mockk()
        getOpponentsUseCase = GetOpponentsUseCase(opponentsRepository)
        mockkStatic(Uri::class)
        mockUri = mockk<Uri>()
    }

    @After
    fun tearDown() {
        unmockkStatic(Uri::class)
    }

    @Test
    fun `Successful response with valid data`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "9503"
        val imageUrlString = "url_a"
        every { Uri.parse(imageUrlString) } returns mockUri

        val expectedOpponent = OpponentTeamDetailDto(
            id = 1,
            name = "Team A",
            imageUrl = imageUrlString.toUri(),
            players = emptyList(),
            acronym = "TA",
            location = "US",
            slug = "team-a",
            modifiedAt = Instant.now()
        )
        val expectedResponseDto = OpponentsResponseDto(opponents = listOf(expectedOpponent))
        val successfulResponse: Response<OpponentsResponseDto> =
            Response.success(expectedResponseDto)

        coEvery { opponentsRepository.getOpponents(idOrSlug) } returns successfulResponse

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Success)
            assertEquals(expectedResponseDto, (result as DomainResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `Successful response with null body`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "valid-id-null-body"
        val nullBodyResponse: Response<OpponentsResponseDto> = Response.success(null)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } returns nullBodyResponse

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals("Empty response body from server.", (result as DomainResult.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `API error with error message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "id-api-error"
        val errorCode = 404
        val errorMessage = "Not Found"
        val errorBody = errorMessage.toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse: Response<OpponentsResponseDto> = Response.error(errorCode, errorBody)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } returns errorResponse

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(
                "API Error: $errorCode - ${errorResponse.message()}",
                (result as DomainResult.Error).message
            )
            awaitComplete()
        }
    }

    @Test
    fun `API error with empty error message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "id-api-empty-error"
        val errorCode = 500
        val errorBody = "".toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse: Response<OpponentsResponseDto> = Response.error(
            errorCode,
            errorBody
        )

        coEvery { opponentsRepository.getOpponents(idOrSlug) } returns errorResponse

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            val expectedMsg = if (errorResponse.message()
                    .isNullOrEmpty()
            ) "Unknown API error" else errorResponse.message()
            assertEquals(
                "API Error: $errorCode - $expectedMsg",
                (result as DomainResult.Error).message
            )
            awaitComplete()
        }
    }


    @Test
    fun `HttpException is thrown`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "id-http-exception"
        val httpExceptionMessage = "HTTP 403 Response.error()"
        val httpException = HttpException(
            Response.error<OpponentsResponseDto>(
                403,
                httpExceptionMessage.toResponseBody("application/json".toMediaTypeOrNull())
            )
        )
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws httpException

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(httpException.localizedMessage, (result as DomainResult.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `HttpException with null localized message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "id-http-exception-null-message"

        class CustomHttpException(response: Response<*>) : HttpException(response) {
            override val message: String? = null
        }

        val httpException = CustomHttpException(
            Response.error<OpponentsResponseDto>(
                500,
                "".toResponseBody("application/json".toMediaTypeOrNull())
            )
        )
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws httpException

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(
                "An unexpected error occurred during HTTP request.",
                (result as DomainResult.Error).message
            )
            awaitComplete()
        }
    }

    @Test
    fun `IOException is thrown`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "id-io-exception"
        val ioExceptionMessage = "Network error"
        val ioException = IOException(ioExceptionMessage)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws ioException

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(ioExceptionMessage, (result as DomainResult.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `IOException with empty message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "id-io-exception-empty-message"
        val ioException = IOException("")
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws ioException

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(
                "Couldn't reach server. Check your internet connection.",
                (result as DomainResult.Error).message
            )
            awaitComplete()
        }
    }

    @Test
    fun `Generic Exception is thrown`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "id-generic-exception"
        val genericExceptionMessage = "Something went wrong"
        val genericException = Exception(genericExceptionMessage)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws genericException

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(genericExceptionMessage, (result as DomainResult.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `Generic Exception with empty message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "id-generic-exception-empty-message"
        val genericException = Exception("")
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws genericException

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals("An unexpected error occurred.", (result as DomainResult.Error).message)
            assertEquals(genericException, result.ex)
            awaitComplete()
        }
    }

    @Test
    fun `Empty idOrSlug input`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = ""
        val errorCode = 400
        val errorMessage = "Bad Request: ID cannot be empty"
        val errorBody = errorMessage.toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse: Response<OpponentsResponseDto> = Response.error(errorCode, errorBody)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } returns errorResponse

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(
                "API Error: $errorCode - ${errorResponse.message()}",
                (result as DomainResult.Error).message
            )
            awaitComplete()
        }
    }

    @Test
    fun `idOrSlug with special characters`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "id with spaces & symbols!"
        val imageUrlString = "url_b"
        every { Uri.parse(imageUrlString) } returns mockUri

        val expectedOpponent = OpponentTeamDetailDto(
            id = 2,
            name = "Team B",
            imageUrl = imageUrlString.toUri(), // This will now use the mocked Uri.parse
            players = emptyList(),
            acronym = "TB",
            location = "EU",
            slug = "team-b",
            modifiedAt = Instant.now()
        )
        val expectedResponseDto = OpponentsResponseDto(opponents = listOf(expectedOpponent))
        val successfulResponse: Response<OpponentsResponseDto> =
            Response.success(expectedResponseDto)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } returns successfulResponse

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Success)
            assertEquals(expectedResponseDto, (result as DomainResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `Multiple emissions check should only be one`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "single-emission-id"
        val imageUrlString = "url_c"
        every { Uri.parse(imageUrlString) } returns mockUri

        val expectedOpponent = OpponentTeamDetailDto(
            id = 3,
            name = "Team C",
            imageUrl = imageUrlString.toUri(), // This will now use the mocked Uri.parse
            players = emptyList(),
            acronym = "TC",
            location = "AS",
            slug = "team-c",
            modifiedAt = Instant.now()
        )
        val expectedResponseDto = OpponentsResponseDto(opponents = listOf(expectedOpponent))
        val successfulResponse: Response<OpponentsResponseDto> =
            Response.success(expectedResponseDto)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } returns successfulResponse

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem() // Collect the first (and supposedly only) item
            assertTrue(result is DomainResult.Success)
            assertEquals(expectedResponseDto, (result as DomainResult.Success).data)
            awaitComplete() // Verifies no more items are emitted
        }
    }
}
