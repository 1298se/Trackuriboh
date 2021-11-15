package sam.g.trackuriboh

import java.util.concurrent.atomic.AtomicBoolean

/**
 * SingleEvent that stops retriggering of events if it's already been handled
 */
data class SingleEvent<out T>(private val content: T) {
    val hasBeenHandled: Boolean
        get() = _hasBeenHandled.get()
    private var _hasBeenHandled = AtomicBoolean(false)

    /**
     * Returns false if the event has already been triggered
     */
    fun handleEvent(): T? {
        return if (_hasBeenHandled.getAndSet(true)) {
            null
        } else {
            content
        }
    }

    fun getContent(): T = content
}
