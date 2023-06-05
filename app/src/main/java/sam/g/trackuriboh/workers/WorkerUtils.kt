package sam.g.trackuriboh.workers

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import sam.g.trackuriboh.di.NetworkModule.MAX_PARALLEL_REQUESTS

const val WORKER_PROGRESS_KEY = "WorkerProgress"

// Around 250 ids seems to be the limit for a GET request with the ids sent in the URL.
// We can send a lot more if it was changed to a POST endpoint
const val GET_REQUEST_ID_QUERY_LIMIT = 250
const val NETWORK_BATCH_REQUEST_DELAY = 250L

suspend fun paginate(
    totalCount: Int,
    paginationSize: Int,
    maxParallelRequests: Int = MAX_PARALLEL_REQUESTS,
    paginate: suspend (offset: Int, paginationSize: Int) -> Unit,
) {

    if (totalCount == 0) {
        return
    }

    // Each bach should make MAX_PARALLEL_REQUESTS number of requests and fetch PAGINATION_LIMIT_SIZE number of items
    // Handle case if the total number of items is smaller than one pagination
    val batchOffsetIncrements = minOf(totalCount, maxParallelRequests * paginationSize)

    for (batchOffset in 0 until totalCount step batchOffsetIncrements) {
        coroutineScope {
            val requestBatch = (
                    batchOffset until minOf(batchOffset + batchOffsetIncrements, totalCount)
                            step paginationSize
                    ).map { curOffset ->
                    async {
                        paginate(curOffset, minOf(paginationSize, totalCount - batchOffset))
                    }
                }

            requestBatch.awaitAll()
            delay(NETWORK_BATCH_REQUEST_DELAY)
        }
    }
}
