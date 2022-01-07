package sam.g.trackuriboh.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.cache.CardSetLocalCache
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.network.responses.CardSetResponse
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.network.services.CardSetApiService
import sam.g.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT
import sam.g.trackuriboh.managers.NetworkRequestHandler
import sam.g.trackuriboh.utils.DATABASE_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardSetRepository @Inject constructor(
    private val cardSetLocalCache: CardSetLocalCache,
    private val cardSetApiService: CardSetApiService,
    private val networkRequestHandler: NetworkRequestHandler,
) {

    suspend fun fetchCardSets(offset: Int = 0, limit: Int = DEFAULT_QUERY_LIMIT) : Resource<CardSetResponse> =
        networkRequestHandler.getTCGPlayerResource { cardSetApiService.getCardSets(offset, limit) }

    suspend fun fetchCardSetDetails(cardSetIds: List<Long>) =
        networkRequestHandler.getTCGPlayerResource { cardSetApiService.getCardSetDetails(cardSetIds.joinToString(",")) }


    fun search(queryString: String?): Flow<PagingData<CardSet>> {
        val pagingSourceFactory = { cardSetLocalCache.searchCardSetByName(queryString) }

        return Pager(
            config = PagingConfig(pageSize = DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getCardSet(setId: Long) = cardSetLocalCache.getCardSet(setId)

    suspend fun insertCardSets(cardSets: List<CardSet>) = cardSetLocalCache.insertCardSets(cardSets)

    suspend fun getCardSetsWithCount() = cardSetLocalCache.getAllCardSetsWithCount()

    fun getSuggestionsCursor(query: String?) = cardSetLocalCache.getSearchSuggestions(query)

}
