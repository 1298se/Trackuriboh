package tang.song.edu.yugiohcollectiontracker.workers

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import tang.song.edu.yugiohcollectiontracker.data.db.CardLocalCache
import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService

class DatabaseSyncWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val cardRetrofitService: CardRetrofitService,
    private val cardLocalCache: CardLocalCache,
) : CoroutineWorker(context, params) {

    companion object {
        private val TAG = DatabaseSyncWorker::class.java.name
    }

    override suspend fun doWork(): Result {
        return try {
            coroutineScope {
                val cardListRequest = async(Dispatchers.IO) { cardRetrofitService.getAllCards() }
                val cardSetListRequest = async(Dispatchers.IO) { cardRetrofitService.getAllSets() }

                val cardListResponse = cardListRequest.await()
                val cardSetListResponse = cardSetListRequest.await()

                Log.d(TAG, "cards from api: " + cardListResponse.data.size)
                Log.d(TAG, "cardSets from api: " + cardSetListResponse.size)

                cardLocalCache.clearDatabase()
                cardLocalCache.populateDatabase(TAG, cardListResponse.data, cardSetListResponse)
            }

            Result.success()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.failure()
        }
    }
}
