package sam.g.trackuriboh.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.DATABASE_PAGE_SIZE
import sam.g.trackuriboh.data.db.ProductLocalCache
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CardRepository @Inject constructor(
    private val productLocalCache: ProductLocalCache,
) {
    fun getSearchResultStream(query: String?): Flow<PagingData<ProductWithCardSetAndSkuIds>> {
        val pagingSourceFactory = { productLocalCache.searchCardByName(query) }

        return Pager(
            config = PagingConfig(pageSize = DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getSearchResultStreamInSet(setId: Long?, query: String?): Flow<PagingData<ProductWithCardSetAndSkuIds>> {
        val pagingSourceFactory = { productLocalCache.searchCardInSetByName(setId, query) }

        return Pager(
            config = PagingConfig(pageSize = DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getCardWithSkusById(id: Long) =
        productLocalCache.getProductWithSkusById(id)
}
