package tang.song.edu.yugiohcollectiontracker.data.repository

import androidx.paging.LivePagedListBuilder
import kotlinx.coroutines.coroutineScope
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

    companion object {
        private const val DATABASE_PAGE_SIZE = 30
    }

    suspend fun getCardDetails(cardId: Long): CardWithSetInfo {
        return cardLocalCache.getCardDetails(cardId)
    }

    suspend fun getCardList(): PagedListBoundaryCallbackResponse<Card> = coroutineScope {
        val dataSourceFactory = cardLocalCache.getCardList()
        val boundaryCallback = CardBoundaryCallback(cardRetrofitService, cardLocalCache, this)

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        PagedListBoundaryCallbackResponse<Card>(data, boundaryCallback.networkErrors)
    }

    suspend fun search(queryString: String): PagedListBoundaryCallbackResponse<Card> = coroutineScope {
        val dataSourceFactory = cardLocalCache.searchCardByName(queryString)
        val boundaryCallback = CardBoundaryCallback(cardRetrofitService, cardLocalCache, this)

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        PagedListBoundaryCallbackResponse<Card>(data, boundaryCallback.networkErrors)
    }
}
