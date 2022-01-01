package sam.g.trackuriboh.data.db.cache

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.map
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.dao.toSearchSuggestionsCursor
import sam.g.trackuriboh.data.db.entities.CardSet
import javax.inject.Inject

class CardSetLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {

    fun searchCardSetByName(name: String?): PagingSource<Int, CardSet> =
        appDatabase.cardSetDao().searchCardSetByName(name ?: "")

    suspend fun getCardSet(setId: Long) =
        appDatabase.cardSetDao().getCardSet(setId)

    suspend fun insertCardSets(cardSets: List<CardSet>) =
        appDatabase.cardSetDao().insert(cardSets)

    suspend fun getCardSetsWithCount() =
        appDatabase.cardSetDao().getCardSetsWithCount()

    fun getSearchSuggestions(query: String?) =
        appDatabase.cardSetDao().getSearchSuggestions(query ?: "").map {
            it.toSearchSuggestionsCursor()
        }
}