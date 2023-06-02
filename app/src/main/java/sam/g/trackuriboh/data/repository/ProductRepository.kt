package sam.g.trackuriboh.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.cache.ProductLocalCache
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.network.services.ProductApiService
import sam.g.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT
import sam.g.trackuriboh.managers.NetworkRequestHandler
import sam.g.trackuriboh.utils.DATABASE_PAGE_SIZE
import sam.g.trackuriboh.workers.GET_REQUEST_ID_QUERY_LIMIT
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productLocalCache: ProductLocalCache,
    private val productApiService: ProductApiService,
    private val networkRequestHandler: NetworkRequestHandler,
    private val priceRepository: PriceRepository,
) {

    companion object {
        private const val TCGPLAYER_PRODUCT_TYPE_CARDS = "Cards"
    }

    suspend fun getProductIdsPaginated(offset: Int, limit: Int = GET_REQUEST_ID_QUERY_LIMIT) =
        productLocalCache.getProductIdsPaginated(offset, limit)

    fun getSearchResultStream(
        query: String?,
        setId: Long?,
        sortOrdering: ProductLocalCache.ProductSortOrdering,
        sortDirection: ProductLocalCache.SortDirection
    ): Flow<PagingData<ProductWithCardSetAndSkuIds>> {
        val pagingSourceFactory = {
            productLocalCache.searchCard(
                ProductLocalCache.ProductSearchOptions(
                    nameQuery = query,
                    setIds = if (setId != null) listOf(setId) else emptyList(),
                    sortOrdering = sortOrdering,
                    sortDirection = sortDirection,
                )
            )
        }

        return Pager(
            config = PagingConfig(pageSize = DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getSuggestionsCursorObservable(query: String?, setId: Long? = null) =
        productLocalCache.getSuggestionsCursorObservable(query, setId)

    suspend fun getProductWithSkusById(id: Long) =
        productLocalCache.getProductWithSkusById(id)

    suspend fun fetchProducts(
        offset: Int = 0,
        limit: Int = DEFAULT_QUERY_LIMIT,
        cardSetId: Long? = null
    ) =
        networkRequestHandler.getTCGPlayerResource {
            productApiService.getProducts(
                listOf(TCGPLAYER_PRODUCT_TYPE_CARDS),
                offset,
                limit,
                cardSetId
            )
        }

    suspend fun upsertProducts(products: List<Product>) =
        productLocalCache.upsertProducts(products)

    suspend fun getCardCount() =
        productLocalCache.getCardCount()

    suspend fun getRarities(query: String?) = productLocalCache.getRarities(query)


    // fun getProductPrintings(name: String): Flow<PagingData<ProductWithCardSetAndSkuIds>> = productLocalCache.getProductPrintings(name)
}
