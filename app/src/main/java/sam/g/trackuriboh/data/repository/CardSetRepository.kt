package sam.g.trackuriboh.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import sam.g.trackuriboh.data.db.ProductLocalCache
import sam.g.trackuriboh.data.db.entities.CardSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardSetRepository @Inject constructor(
    private val productLocalCache: ProductLocalCache
) {

    fun search(queryString: String?): Flow<PagingData<CardSet>> {
        val pagingSourceFactory = { productLocalCache.searchCardSetByName(queryString) }

        return Pager(
            config = PagingConfig(pageSize = sam.g.trackuriboh.DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow.flowOn(Dispatchers.IO)
    }
}
