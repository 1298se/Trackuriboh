package tang.song.edu.trackuriboh.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import tang.song.edu.trackuriboh.DATABASE_PAGE_SIZE
import tang.song.edu.trackuriboh.data.db.CardLocalCache
import tang.song.edu.trackuriboh.data.db.entities.Card
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CardRepository @Inject constructor(
    private val cardLocalCache: CardLocalCache,
) {
    fun getSearchResultStream(queryString: String?): Flow<PagingData<Card>> {
        val pagingSourceFactory = { cardLocalCache.searchCardByName(queryString) }

        return Pager(
            config = PagingConfig(pageSize = DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow.flowOn(Dispatchers.IO)
    }
}
