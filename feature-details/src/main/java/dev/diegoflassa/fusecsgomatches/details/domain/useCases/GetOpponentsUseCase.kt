package dev.diegoflassa.fusecsgomatches.details.domain.useCases

import dev.diegoflassa.fusecsgomatches.details.data.dto.OpponentsResponseDto
import dev.diegoflassa.fusecsgomatches.details.data.repository.interfaces.IOpponentsRepository
import dev.diegoflassa.fusecsgomatches.core.domain.model.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetOpponentsUseCase @Inject constructor(
    private val opponentsRepository: IOpponentsRepository
): IGetOpponentsUseCase {
    override operator fun invoke(idOrSlug: String): Flow<DomainResult<OpponentsResponseDto>> = flow {
        try {
            val response = opponentsRepository.getOpponents(idOrSlug)
            if (response.isSuccessful) {
                val opponentsData = response.body()
                if (opponentsData != null) {
                    emit(DomainResult.Success(opponentsData))
                } else {
                    emit(DomainResult.Error("Empty response body from server."))
                }
            } else {
                emit(
                    DomainResult.Error(
                        "API Error: ${response.code()} - ${
                            response.message().ifEmpty { "Unknown API error" }
                        }"
                    )
                )
            }
        } catch (e: HttpException) {
            val message = e.localizedMessage
            val finalMessage = if (message.isNullOrBlank()) {
                "An unexpected error occurred during HTTP request."
            } else {
                message
            }
            emit(DomainResult.Error(finalMessage, e))
        } catch (e: IOException) {
            val message = e.message
            val finalMessage = if (message.isNullOrBlank()) {
                "Couldn't reach server. Check your internet connection."
            } else {
                message
            }
            emit(DomainResult.Error(finalMessage, e))
        } catch (e: Exception) {
            val message = e.message
            val finalMessage = if (message.isNullOrBlank()) {
                "An unexpected error occurred."
            } else {
                message
            }
            emit(DomainResult.Error(finalMessage, e))
        }
    }
}