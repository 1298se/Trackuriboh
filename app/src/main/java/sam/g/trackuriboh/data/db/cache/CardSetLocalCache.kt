package sam.g.trackuriboh.data.db.cache

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.map
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.dao.toSearchSuggestionsCursor
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardSetLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {

    private var recentCardSetsWithCards: Map<CardSet, Map<Product, Double?>> = emptyMap()

    fun searchCardSetByName(name: String?): PagingSource<Int, CardSet> =
        appDatabase.cardSetDao().searchCardSetByName(name ?: "")

    suspend fun getCardSet(setId: Long) =
        appDatabase.cardSetDao().getCardSet(setId)

    suspend fun upsertCardSets(cardSets: List<CardSet>) =
        appDatabase.cardSetDao().upsert(cardSets)

    suspend fun getAllCardSetsWithCount() =
        appDatabase.cardSetDao().getAllCardSetsWithCount()

    fun getSearchSuggestions(query: String?) =
        appDatabase.cardSetDao().getSearchSuggestions(query ?: "").map {
            it.toSearchSuggestionsCursor()
        }

    suspend fun getRecentCardSetsAndMostExpensiveCards(numSets: Int, numCards: Int) =
        appDatabase.cardSetDao().getRecentCardSetsAndMostExpensiveCards(numSets, numCards)

    suspend fun getTotalCardSetCount() =
        appDatabase.cardSetDao().getTotalCount()
}