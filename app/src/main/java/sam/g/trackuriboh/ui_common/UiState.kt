package sam.g.trackuriboh.ui_common

/**
 * UI state for success/loading/failure
 */
sealed class UiState<out T>(open val data: T? = null, open val errorMessage: String? = null) {
    data class Success<T>(override val data: T?): UiState<T>(data)
    object Loading: UiState<Nothing>()
    data class Failure<T>(
        override val errorMessage: String?,
        override val data: T? = null,
    ): UiState<T>(errorMessage = errorMessage, data = data)
}
