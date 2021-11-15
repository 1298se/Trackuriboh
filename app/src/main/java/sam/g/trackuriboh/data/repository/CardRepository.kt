package sam.g.trackuriboh.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import sam.g.trackuriboh.DATABASE_PAGE_SIZE
import sam.g.trackuriboh.data.db.ProductLocalCache
import sam.g.trackuriboh.data.db.relations.ProductWithSetAndSkuIds
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CardRepository @Inject constructor(
    private val productLocalCache: ProductLocalCache,
) {
    fun getSearchResultStream(query: String?): Flow<PagingData<ProductWithSetAndSkuIds>> {
        val pagingSourceFactory = { productLocalCache.searchCardByName(query) }

        return Pager(
            config = PagingConfig(pageSize = DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow.flowOn(Dispatchers.IO)
    }

    suspend fun getCardWithSkusById(id: Long) =
        productLocalCache.getProductWithSkusById(id)
}