package tang.song.edu.yugiohcollectiontracker.data.db

import androidx.paging.DataSource
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardXCardSetRef
import javax.inject.Inject

class CardLocalCache @Inject constructor(
    private val cardDatabase: CardDatabase
) {
    suspend fun insertCards(cardList: List<Card>): List<Long> {
        return cardDatabase.cardDao().insertCards(cardList)
    }

    suspend fun insertCardSets(cardSetList: List<CardSet>): List<Long> {
        return cardDatabase.cardSetDao().insertCardSets(cardSetList)
    }

    suspend fun insertCardXCardSets(joinList: List<CardXCardSetRef>): List<Long> {
        return cardDatabase.cardXCardSetDao().insertJoins(joinList)
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