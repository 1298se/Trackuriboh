package tang.song.edu.yugiohcollectiontracker.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import tang.song.edu.yugiohcollectiontracker.data.db.CardLocalCache
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardSetRepository @Inject constructor(
    private val cardLocalCache: CardLocalCache
) {

    companion object {
        private const val DATABASE_PAGE_SIZE = 30
    }

    fun getCardSetList(): LiveData<PagedList<CardSet>> {
        val dataSourceFactory = cardLocalCache.getCardSetList()

        return LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .build()
    }

    fun search(queryString: String): LiveData<PagedList<CardSet>> {
        val dataSourceFactory = cardLocalCache.searchCardSetByName(queryString)

        return LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .build()
    }
}
