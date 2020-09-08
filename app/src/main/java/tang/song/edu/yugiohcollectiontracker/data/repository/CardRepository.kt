package tang.song.edu.yugiohcollectiontracker.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.coroutineScope
import tang.song.edu.yugiohcollectiontracker.DATABASE_PAGE_SIZE
import tang.song.edu.yugiohcollectiontracker.data.db.CardLocalCache
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo
import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService
import tang.song.edu.yugiohcollectiontracker.data.network.PagedListBoundaryCallbackResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val cardLocalCache: CardLocalCache,
    private val cardRetrofitService: CardRetrofitService
) {

    suspend fun getCardDetails(cardId: Long): CardWithSetInfo {
        return cardLocalCache.getCardDetails(cardId)
    }

    suspend fun getCardList(): PagedListBoundaryCallbackResponse<Card> = coroutineScope {
        val dataSourceFactory = cardLocalCache.getCardList()
        val boundaryCallback = CardBoundaryCallback(cardRetrofitService, cardLocalCache, this)

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        PagedListBoundaryCallbackResponse(data, boundaryCallback.networkErrors)
    }

    suspend fun search(queryString: String): PagedListBoundaryCallbackResponse<Card> = coroutineScope {
        val dataSourceFactory = cardLocalCache.searchCardByName(queryString)
        val boundaryCallback = CardBoundaryCallback(cardRetrofitService, cardLocalCache, this)

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        PagedListBoundaryCallbackResponse(data, boundaryCallback.networkErrors)
    }

    fun getCardListBySet(setCode: String): LiveData<PagedList<Card>> {
        val dataSourceFactory = cardLocalCache.getCardListBySet(setCode)

        return LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .build()
    }
}
