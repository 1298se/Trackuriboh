package sam.g.trackuriboh.data.network.responses

sealed class Resource<out T>(open val data: T? = null, open val exception: Exception? = null) {
    data class Success<T>(override val data: T): Resource<T>(data = data)
    data class Failure<T>(
        override  val exception: Exception,
        override val data: T? = null
    ): Resource<T>(exception = exception, data = data)
}
