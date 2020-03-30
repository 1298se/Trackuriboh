package tang.song.edu.yugiohcollectiontracker.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.data.db.CardDatabase
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSetXRef
import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.SetResponse
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class DatabaseSyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    @Inject
    lateinit var cardRetrofitService: CardRetrofitService
    @Inject
    lateinit var cardDatabase: CardDatabase

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
                    populateDatabase(cardResponse.body(), setResponse.body())
                } else {
                    throw IOException(cardResponse.message())
                }
            }

            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }

    private suspend fun populateDatabase(
        cardList: List<CardResponse>?,
        setList: List<SetResponse>?
    ) {
        Log.d(TAG, "cards from api: " + cardList?.size)
        Log.d(TAG, "cardSets from api: " + setList?.size)

        if (cardList != null && setList != null) {
            val cardInsertNum =
                cardDatabase.cardDao().insertCards(convertCardResponseListToCardList(cardList))
            Log.d(TAG, "cards inserted to db: ${cardInsertNum.size}")

            val setInsertNum =
                cardDatabase.cardSetDao().insertSets(convertSetResponseListToSetList(setList))
            Log.d(TAG, "card sets inserted to db: ${setInsertNum.size}")

            val joinInsertNum = cardDatabase.cardXCardSetDao().insertJoins(createJoins(cardList))
            Log.d(TAG, "joins inserted to db: ${joinInsertNum.size}")
        }
    }

    private suspend fun convertCardResponseListToCardList(cardList: List<CardResponse>): List<Card> =
        withContext(Dispatchers.Default) {
            val result = ArrayList<Card>()

            for (card in cardList) {
                result.add(
                    Card(
                        card.id,
                        card.name,
                        card.type,
                        card.desc,
                        card.atk,
                        card.def,
                        card.level,
                        card.race,
                        card.attribute,
                        card.archetype,
                        card.scale,
                        card.cardImages?.get(0)?.imageUrlSmall
                    )
                )
            }
            result
        }

    private suspend fun convertSetResponseListToSetList(setList: List<SetResponse>): List<CardSet> =
        withContext(Dispatchers.Default) {

            val result = ArrayList<CardSet>()

            for (set in setList) {
                result.add(
                    CardSet(
                        set.setCode,
                        set.setName,
                        set.size,
                        set.releaseDate
                    )
                )
            }
            result
        }

    private suspend fun createJoins(cardList: List<CardResponse>): List<CardSetXRef> =
        withContext(Dispatchers.Default) {

            val result = ArrayList<CardSetXRef>()

            for (card in cardList) {
                for (cardSet in card.cardSets ?: Collections.emptyList()) {
                    result.add(CardSetXRef(card.id, parseCardSet(cardSet), cardSet.setRarity))
                }
            }

            result
        }

    private fun parseCardSet(cardSet: CardSetResponse): String {
        val hyphenIndex = cardSet.setCode.indexOf('-')

        return if (hyphenIndex != -1) cardSet.setCode.substring(0, hyphenIndex) else cardSet.setCode
    }
}
