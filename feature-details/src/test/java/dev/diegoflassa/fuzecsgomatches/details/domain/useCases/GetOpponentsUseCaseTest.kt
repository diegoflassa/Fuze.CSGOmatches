package dev.diegoflassa.fuzecsgomatches.details.domain.useCases

import android.net.Uri
import androidx.core.net.toUri
import app.cash.turbine.test
import dev.diegoflassa.fuzecsgomatches.core.domain.model.DomainResult
import dev.diegoflassa.fuzecsgomatches.details.data.dto.OpponentTeamDetailDto
import dev.diegoflassa.fuzecsgomatches.details.data.dto.OpponentsResponseDto
import dev.diegoflassa.fuzecsgomatches.details.data.repository.interfaces.IOpponentsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class GetOpponentsUseCaseTest {

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
        unmockkStatic(Uri::class) // Important to clean up static mocks
    }

    @Suppress("SameParameterValue")
    private fun createDummyOpponentDto(id: Int, name: String, imageUrlString: String): OpponentTeamDetailDto {
        every { Uri.parse(imageUrlString) } returns mockUri
        return OpponentTeamDetailDto(
            id = id,
            name = name,
            imageUrl = imageUrlString.toUri(),
            players = emptyList(),
            acronym = name.take(3).uppercase(),
            location = "US",
            slug = name.replace(" ", "-").lowercase(),
            modifiedAt = Instant.now()
        )
    }

    @Test
    fun `invoke with valid idOrSlug returns Success result with data`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "valid-team"
        val opponentDto = createDummyOpponentDto(1, "Team Valid", "http://example.com/image.png")
        val expectedResponseDto = OpponentsResponseDto(opponents = listOf(opponentDto))
        val successfulResponse: Response<OpponentsResponseDto> = Response.success(expectedResponseDto)

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
    fun `invoke returns Error when response body is null`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "null-body-team"
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
    fun `invoke returns Error on API error with message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "api-error-team"
        val errorCode = 404
        val errorMessage = "Not Found"
        val errorBody = errorMessage.toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse: Response<OpponentsResponseDto> = Response.error(errorCode, errorBody)
        val expectedRetrofitMessage = errorResponse.message().ifEmpty { "Unknown API error" }


        coEvery { opponentsRepository.getOpponents(idOrSlug) } returns errorResponse

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(
                "API Error: $errorCode - $expectedRetrofitMessage",
                (result as DomainResult.Error).message
            )
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns Error on API error with empty message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "api-error-empty-message-team"
        val errorCode = 500
        val errorResponseWithoutMessage: Response<OpponentsResponseDto> = mockk {
            every { isSuccessful } returns false
            every { code() } returns errorCode
            every { message() } returns ""
            every { body() } returns null
            every { errorBody() } returns "".toResponseBody("application/json".toMediaTypeOrNull())
        }
        coEvery { opponentsRepository.getOpponents(idOrSlug) } returns errorResponseWithoutMessage

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(
                "API Error: $errorCode - Unknown API error",
                (result as DomainResult.Error).message
            )
            awaitComplete()
        }
    }


    @Test
    fun `invoke returns Error on HttpException with localized message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "http-exception-team"
        val httpExceptionMessage = "HTTP 403 Forbidden"
        val responseErrorBody = "Actual error content".toResponseBody("application/json".toMediaTypeOrNull())
        val mockResponse = Response.error<OpponentsResponseDto>(403, responseErrorBody)

        val httpException = mockk<HttpException>()
        every { httpException.localizedMessage } returns httpExceptionMessage
        every { httpException.code() } returns 403
        every { httpException.message() } returns "Client Error"
        every { httpException.response() } returns mockResponse


        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws httpException

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(httpExceptionMessage, (result as DomainResult.Error).message)
            assertEquals(httpException, result.ex)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns default Error on HttpException with null or blank localized message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "http-exception-null-message-team"
        val responseErrorBody = "".toResponseBody("application/json".toMediaTypeOrNull())
        val mockResponse = Response.error<OpponentsResponseDto>(500, responseErrorBody)

        val httpExceptionWithNullMessage = mockk<HttpException>()
        every { httpExceptionWithNullMessage.localizedMessage } returns null
        every { httpExceptionWithNullMessage.code() } returns 500
        every { httpExceptionWithNullMessage.message() } returns "Server Error"
        every { httpExceptionWithNullMessage.response() } returns mockResponse


        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws httpExceptionWithNullMessage

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals("An unexpected error occurred during HTTP request.", (result as DomainResult.Error).message)
            assertEquals(httpExceptionWithNullMessage, result.ex)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns Error on IOException with message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "io-exception-team"
        val ioExceptionMessage = "Network error, please try again"
        val ioException = IOException(ioExceptionMessage)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws ioException

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(ioExceptionMessage, (result as DomainResult.Error).message)
            assertEquals(ioException, result.ex)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns default Error on IOException with null or blank message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "io-exception-null-message-team"
        val ioExceptionWithNullMessage = IOException(null as String?)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws ioExceptionWithNullMessage

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals("Couldn't reach server. Check your internet connection.", (result as DomainResult.Error).message)
            assertEquals(ioExceptionWithNullMessage, result.ex)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns Error on generic Exception with message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "generic-exception-team"
        val genericExceptionMessage = "Something totally unexpected happened"
        val genericException = Exception(genericExceptionMessage)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws genericException

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals(genericExceptionMessage, (result as DomainResult.Error).message)
            assertEquals(genericException, result.ex)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns default Error on generic Exception with null or blank message`() = runTest(testDispatcher) {
        // Given
        val idOrSlug = "generic-exception-null-message-team"
        val genericExceptionWithNullMessage = Exception(null as String?)
        coEvery { opponentsRepository.getOpponents(idOrSlug) } throws genericExceptionWithNullMessage

        // When & Then
        getOpponentsUseCase(idOrSlug).test {
            val result = awaitItem()
            assertTrue(result is DomainResult.Error)
            assertEquals("An unexpected error occurred.", (result as DomainResult.Error).message)
            assertEquals(genericExceptionWithNullMessage, result.ex)
            awaitComplete()
        }
    }
}
