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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productLocalCache: ProductLocalCache,
    private val productApiService: ProductApiService,
    private val networkRequestHandler: NetworkRequestHandler
) {

    companion object {
        private const val TCGPLAYER_PRODUCT_TYPE_CARDS = "Cards"
    }

    fun getSearchResultStream(query: String?): Flow<PagingData<ProductWithCardSetAndSkuIds>> {
        val pagingSourceFactory = { productLocalCache.searchCardByName(query) }

        return Pager(
            config = PagingConfig(pageSize = DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getSearchResultStreamInSet(
        setId: Long,
        query: String?
    ): Flow<PagingData<ProductWithCardSetAndSkuIds>> {
        val pagingSourceFactory = { productLocalCache.searchCardInSetByName(setId, query) }

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

    suspend fun getTotalCardCount() =
        productLocalCache.getTotalCardCount()

    suspend fun getRarities(query: String?) = productLocalCache.getRarities(query)


    // fun getProductPrintings(name: String): Flow<PagingData<ProductWithCardSetAndSkuIds>> = productLocalCache.getProductPrintings(name)
}
