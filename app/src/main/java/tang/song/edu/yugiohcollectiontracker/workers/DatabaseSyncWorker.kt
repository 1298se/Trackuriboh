package tang.song.edu.yugiohcollectiontracker.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.data.db.CardLocalCache
import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService
import java.io.IOException
import javax.inject.Inject

class DatabaseSyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    @Inject
    lateinit var cardRetrofitService: CardRetrofitService
    @Inject
    lateinit var cardLocalCache: CardLocalCache

    companion object {
        private val TAG = DatabaseSyncWorker::class.java.name
    }

    init {
        (context.applicationContext as BaseApplication).appComponent.inject(this)
    }

    override suspend fun doWork(): Result {
        return try {
            coroutineScope {
                val cardList = async(Dispatchers.IO) { cardRetrofitService.getAllCards() }
                val setList = async(Dispatchers.IO) { cardRetrofitService.getAllSets() }

                val cardResponse = cardList.await()
                val setResponse = setList.await()

                if (cardResponse.isSuccessful && setResponse.isSuccessful) {
                    cardLocalCache.populateDatabase(TAG, cardResponse.body(), setResponse.body())
                } else {
                    throw IOException(cardResponse.message())
                }
            }

            Result.success()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.failure()
        }
    }
}
