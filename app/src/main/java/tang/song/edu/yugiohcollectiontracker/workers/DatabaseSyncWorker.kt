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
import tang.song.edu.yugiohcollectiontracker.data.db.CardLocalCache
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardXCardSetRef
import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetDetailResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetResponse
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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
        cardSetList: List<CardSetResponse>?
    ) {
        Log.d(TAG, "cards from api: " + cardList?.size)
        Log.d(TAG, "cardSets from api: " + cardSetList?.size)

        if (cardList != null && cardSetList != null) {
            val cardInsertNum = cardLocalCache.insertCards(convertCardResponseListToCardList(cardList))
            Log.d(TAG, "cards inserted to db: ${cardInsertNum.size}")

            val setInsertNum = cardLocalCache.insertCardSets(convertSetResponseListToSetList(cardSetList))
            Log.d(TAG, "card sets inserted to db: ${setInsertNum.size}")

            val joinInsertNum = cardLocalCache.insertCardXCardSets(createJoins(cardList))
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

    private suspend fun convertSetResponseListToSetList(cardSetList: List<CardSetResponse>): List<CardSet> =
        withContext(Dispatchers.Default) {

            val result = ArrayList<CardSet>()

            for (set in cardSetList) {
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

    private suspend fun createJoins(cardList: List<CardResponse>): List<CardXCardSetRef> =
        withContext(Dispatchers.Default) {

            val result = ArrayList<CardXCardSetRef>()

            for (card in cardList) {
                for (cardSet in card.cardSetDetails ?: Collections.emptyList()) {
                    result.add(CardXCardSetRef(card.id, parseCardSet(cardSet), cardSet.setRarity))
                }
            }

            result
        }

    private fun parseCardSet(cardSetDetail: CardSetDetailResponse): String {
        val hyphenIndex = cardSetDetail.setCode.indexOf('-')

        return if (hyphenIndex != -1) cardSetDetail.setCode.substring(0, hyphenIndex) else cardSetDetail.setCode
    }
}
