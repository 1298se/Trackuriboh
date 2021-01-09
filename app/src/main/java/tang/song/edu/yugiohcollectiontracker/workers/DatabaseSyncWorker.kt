package tang.song.edu.yugiohcollectiontracker.workers

import android.content.Context
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
                val cardList = async(Dispatchers.IO) { cardRetrofitService.getAllCards() }
                val setList = async(Dispatchers.IO) { cardRetrofitService.getAllSets() }

                val cardResponse = cardList.await()
                val setResponse = setList.await()

                cardLocalCache.populateDatabase(TAG, cardResponse.data, setResponse)
            }

            Result.success()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.failure()
        }
    }
}
