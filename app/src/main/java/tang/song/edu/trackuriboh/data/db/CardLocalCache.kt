package tang.song.edu.trackuriboh.data.db

import androidx.paging.PagingSource
import tang.song.edu.trackuriboh.data.db.entities.Card
import tang.song.edu.trackuriboh.data.db.entities.CardSet
import javax.inject.Inject

class CardLocalCache @Inject constructor(
    private val cardDatabase: CardDatabase
) {
    fun searchCardByName(name: String?): PagingSource<Int, Card> {
        val query = "%${(name ?: "").replace(' ', '%')}%"
        return cardDatabase.cardDao().searchCardByName(query)
    }

    fun searchCardSetByName(name: String?): PagingSource<Int, CardSet> {
        val query = "%${(name ?: "").replace(' ', '%')}%"
        return cardDatabase.cardSetDao().searchCardSetByName(query)
    }

    suspend fun insertCardSets(cardSets: List<CardSet>): List<Long> =
        cardDatabase.cardSetDao().insertCardSets(cardSets)

    suspend fun insertCards(cards: List<Card>): List<Long> =
        cardDatabase.cardDao().insertCards(cards)
}
