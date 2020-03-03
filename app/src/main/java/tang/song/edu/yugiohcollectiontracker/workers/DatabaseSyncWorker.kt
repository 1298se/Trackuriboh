package tang.song.edu.yugiohcollectiontracker.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import tang.song.edu.yugiohcollectiontracker.data.db.CardDatabase
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.SetResponse
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.SetRepository

class DatabaseSyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    lateinit var cardRepository: CardRepository
    lateinit var setRepository: SetRepository
    lateinit var cardDatabase: CardDatabase

    override suspend fun doWork(): Result {
        val cardDao = cardDatabase.cardDao()
        val setDao = cardDatabase.setDao()

        withContext(Dispatchers.IO) {
            val cardList = async { cardRepository.getAllCards() }
            val setList = async { setRepository.getAllSets() }

            populateDatabase(cardList.await(), setList.await().data)
        }


        return Result.success()
    }

    private suspend fun populateDatabase(
        cardList: List<CardResponse>?,
        setList: List<SetResponse>?
    ) {

    }

    private suspend fun convertCardResponseToCard(cardList: List<CardResponse>) {
        withContext(Dispatchers.Default) {
            for (card in cardList) {

            }
        }
    }

}