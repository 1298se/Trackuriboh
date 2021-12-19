package sam.g.trackuriboh.data.network.responses

import java.io.IOException

sealed class Resource<out T>(open val data: T? = null, open val exception: Exception? = null) {
    data class Success<T>(override val data: T): Resource<T>(data = data)
    data class Failure<T>(
        override  val exception: Exception,
        override val data: T? = null
    ): Resource<T>(exception = exception, data = data)
    object Offline : Resource<Nothing>()

    fun getResponseOrThrow() : T {
        if (this is Success) {
            return data
        } else {
            throw IOException(exception)
        }
    }
}
