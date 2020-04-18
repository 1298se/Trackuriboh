package tang.song.edu.yugiohcollectiontracker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.data.db.CardLocalCache
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService
import tang.song.edu.yugiohcollectiontracker.workers.DatabaseSyncWorker

class CardBoundaryCallback(
    private val service: CardRetrofitService,
    private val cache: CardLocalCache,
    private val scope: CoroutineScope
) : PagedList.BoundaryCallback<Card>() {
    private var isRequestInProgress = false
    private val _networkErrors = MutableLiveData<String>()
    val networkErrors: LiveData<String>
        get() = _networkErrors

    companion object {
        private val TAG = DatabaseSyncWorker::class.java.name
    }

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Card) {
        requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        scope.launch {
            val cardList = async(Dispatchers.IO) { service.getAllCards() }
            val setList = async(Dispatchers.IO) { service.getAllSets() }

            val cardResponse = cardList.await()
            val setResponse = setList.await()

            if (cardResponse.isSuccessful && setResponse.isSuccessful) {
                cache.populateDatabase(TAG, cardResponse.body(), setResponse.body())
            }
        }
    }
}
