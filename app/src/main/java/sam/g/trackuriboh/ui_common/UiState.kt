package sam.g.trackuriboh.ui_common

/**
 * UI state for success/loading/failure
 */
sealed class UiState<out T>(open val data: T? = null, open val message: String? = null) {
    data class Success<T>(
        override val data: T?,
        override val message: String? = null
    ): UiState<T>(data, message)

    data class Loading<T>(
        override val data: T? = null,
        override val message: String? = null
    ): UiState<T>(data, message)

    data class Failure<T>(
        override val data: T? = null,
        override val message: String,
    ): UiState<T>(message = message, data = data)
}
