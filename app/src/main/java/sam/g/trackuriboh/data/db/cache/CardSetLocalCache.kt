package sam.g.trackuriboh.data.db.cache

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.map
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.dao.toSearchSuggestionsCursor
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product
import javax.inject.Inject

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

    suspend fun getRecentSetsWithCardsSortedByPrice(numSets: Int, numCards: Int, shouldRefresh: Boolean): Map<CardSet, Map<Product, Double?>> {
        if (recentCardSetsWithCards.isEmpty() || shouldRefresh) {
            val map = mutableMapOf<CardSet, Map<Product, Double?>>()

            val recentCardSets = appDatabase.cardSetDao().getRecentCardSets(numSets)

            for (cardSet in recentCardSets) {
                val productWithLowestListing = appDatabase.productDao().getMostExpensiveProductsInSet(cardSet.id, numCards)

                map[cardSet] = productWithLowestListing
            }

            return map.also { recentCardSetsWithCards = it }
        }

        return recentCardSetsWithCards
    }

    suspend fun getTotalCardSetCount() =
        appDatabase.cardSetDao().getTotalCount()
}