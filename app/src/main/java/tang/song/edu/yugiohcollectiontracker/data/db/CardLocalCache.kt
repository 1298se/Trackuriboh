package tang.song.edu.yugiohcollectiontracker.data.db

import android.util.Log
import androidx.paging.DataSource
import tang.song.edu.yugiohcollectiontracker.ResponseUtils
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardXCardSetRef
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetResponse
import javax.inject.Inject

class CardLocalCache @Inject constructor(
    private val cardDatabase: CardDatabase
) {
    suspend fun populateDatabase(
        tag: String,
        cardList: List<CardResponse>?,
        cardSetList: List<CardSetResponse>?
    ) {
        Log.d(tag, "cards from api: " + cardList?.size)
        Log.d(tag, "cardSets from api: " + cardSetList?.size)

        if (cardList != null && cardSetList != null) {
            val cardInsertNum = insertCards(ResponseUtils.convertCardResponseListToCardList(cardList))
            Log.d(tag, "cards inserted to db: ${cardInsertNum.size}")

            val setInsertNum = insertCardSets(ResponseUtils.convertSetResponseListToSetList(cardSetList))
            Log.d(tag, "card sets inserted to db: ${setInsertNum.size}")

            val joinInsertNum = insertCardXCardSets(ResponseUtils.createJoins(cardList))
            Log.d(tag, "joins inserted to db: ${joinInsertNum.size}")
        }
    }

    private suspend fun insertCards(cardList: List<Card>): List<Long> {
        return cardDatabase.cardDao().insertCards(cardList)
    }

    private suspend fun insertCardSets(cardSetList: List<CardSet>): List<Long> {
        return cardDatabase.cardSetDao().insertCardSets(cardSetList)
    }

    private suspend fun insertCardXCardSets(joinList: List<CardXCardSetRef>): List<Long> {
        return cardDatabase.cardXCardSetDao().insertJoins(joinList)
    }

    suspend fun getCardDetails(cardId: Long): CardWithSetInfo {
        return cardDatabase.cardXCardSetDao().getCardWithSetInfo(cardId)
    }

    fun getCardList(): DataSource.Factory<Int, Card> {
        return cardDatabase.cardDao().getCardList()
    }

    fun getCardSetList(): DataSource.Factory<Int, CardSet> {
        return cardDatabase.cardSetDao().getCardSetList()
    }

    fun searchCardByName(name: String): DataSource.Factory<Int, Card> {
        val query = "%${name.replace(' ', '%')}%"
        return cardDatabase.cardDao().searchCardByName(query)
    }

    fun searchCardSetByName(name: String): DataSource.Factory<Int, CardSet> {
        val query = "%${name.replace(' ', '%')}%"
        return cardDatabase.cardSetDao().searchCardSetByName(query)
    }
}