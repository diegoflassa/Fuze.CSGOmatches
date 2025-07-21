package dev.diegoflassa.fusecsgomatches.core.domain.model

sealed class DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Error(val message: String, val ex: Exception? = null) : DomainResult<Nothing>()
}
