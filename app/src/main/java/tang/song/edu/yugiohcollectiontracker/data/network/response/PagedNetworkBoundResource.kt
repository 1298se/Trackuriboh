package tang.song.edu.yugiohcollectiontracker.data.network.response

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class PagedNetworkBoundResource<ResultType, RequestType> {

    fun asFlow() = flow {
        withContext(Dispatchers.IO) {
            emit(Resource.loading())
            val dbValue = loadFromDb()

            if (shouldFetch(dbValue)) {
                try {
                    val apiResponse = createCall()
                    saveCallResult(apiResponse)
                    emit(Resource.success(loadFromDb()))
                } catch (e: Exception) {
                    emit(
                        when (e) {
                            is HttpException -> Resource.error(getErrorMessage(e.code()), null)
                            else -> Resource.error(getErrorMessage(Int.MAX_VALUE), null)
                        }
                    )
                }
            }
            emit(Resource.success(dbValue))
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            401 -> "Unauthorised"
            404 -> "Not found"
            else -> "Something went wrong"
        }
    }

    protected open fun onFetchFailed() {
    }

    protected abstract suspend fun saveCallResult(item: RequestType?)

    protected abstract suspend fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun loadFromDb(): ResultType

    protected abstract suspend fun createCall(): RequestType
}