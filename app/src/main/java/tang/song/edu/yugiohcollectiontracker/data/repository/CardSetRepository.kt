package tang.song.edu.yugiohcollectiontracker.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tang.song.edu.yugiohcollectiontracker.data.db.CardLocalCache
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardSetRepository @Inject constructor(
    private val cardLocalCache: CardLocalCache
) {


    fun search(queryString: String?): Flow<PagingData<CardSet>> {
        val pagingSourceFactory = { cardLocalCache.searchCardSetByName(queryString) }

        return Pager(
            config = PagingConfig(pageSize = tang.song.edu.yugiohcollectiontracker.DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getCardSet(setName: String) = cardLocalCache.getCardSet(setName)
}
