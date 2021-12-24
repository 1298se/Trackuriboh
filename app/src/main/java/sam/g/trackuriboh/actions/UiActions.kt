package sam.g.trackuriboh.actions

sealed interface UiAction

data class RequestPermission(val action: String) : UiAction