package sam.g.trackuriboh.workers

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import sam.g.trackuriboh.di.NetworkModule.MAX_PARALLEL_REQUESTS

const val REMINDER_SCHEDULER_CHANNEL_ID = "Trackuriboh"

const val DB_SYNC_NOTIFICATION_CHANNEL_ID = "TrackuribohDBSync"

const val DB_SYNC_PROGRESS_NOTIFICATION_ID = 1
// We need two separate IDs for progress and state because the progress notif will automatically disappear after the Work is done
// because it is provided in Worker.getForegroundInfo
const val DB_SYNC_STATE_NOTIFICATION_ID = 2

const val WORKER_PROGRESS_KEY = "WorkerProgress"


suspend fun <T> paginate(
    totalCount: Int,
    paginationSize: Int,
    maxParallelRequests: Int = MAX_PARALLEL_REQUESTS,
    paginate: suspend (offset: Int, paginationSize: Int) -> List<T>,
    onPaginate: suspend (offset: Int, List<T>) -> Unit,
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
                        val itemList = paginate(curOffset, paginationSize)

                        onPaginate(batchOffset, itemList)
                    }
                }

            requestBatch.awaitAll()
        }
    }
}