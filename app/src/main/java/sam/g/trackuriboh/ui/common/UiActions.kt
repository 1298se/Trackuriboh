package sam.g.trackuriboh.ui.common

sealed interface UiAction

data class RequestPermission(val action: String) : UiAction