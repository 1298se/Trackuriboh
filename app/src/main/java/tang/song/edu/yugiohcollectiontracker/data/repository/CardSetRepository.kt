package tang.song.edu.yugiohcollectiontracker.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import kotlinx.coroutines.coroutineScope
import tang.song.edu.yugiohcollectiontracker.data.db.CardLocalCache
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.network.PagedListBoundaryCallbackResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardSetRepository @Inject constructor(
    private val cardLocalCache: CardLocalCache
) {

    companion object {
        private const val DATABASE_PAGE_SIZE = 30
    }

    suspend fun getCardSetList(): PagedListBoundaryCallbackResponse<CardSet> = coroutineScope {
        val dataSourceFactory = cardLocalCache.getCardSetList()

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .build()

        PagedListBoundaryCallbackResponse(data, MutableLiveData())
    }

    suspend fun search(queryString: String): PagedListBoundaryCallbackResponse<CardSet> = coroutineScope {
        val dataSourceFactory = cardLocalCache.searchCardSetByName(queryString)

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .build()

        PagedListBoundaryCallbackResponse(data, MutableLiveData())
    }
}
