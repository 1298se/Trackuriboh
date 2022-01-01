package sam.g.trackuriboh.ui.common.actions

sealed interface UiAction

data class RequestPermission(val action: String) : UiAction