package sam.g.trackuriboh.data.db.cache

import kotlinx.coroutines.flow.map
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.dao.getFuzzySearchQuery
import sam.g.trackuriboh.data.db.dao.toSearchSuggestionsCursor
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.types.ProductType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    data class ProductSearchOptions(
        val nameQuery: String? = null,
        val productTypes: List<ProductType> = emptyList(),
        val setIds: List<Long> = emptyList(),
        val rarities: List<String> = emptyList(),
        val cardTypes: List<String> = emptyList(),
        val sortOrdering: ProductSortOrdering = ProductSortOrdering.BEST_MATCH,
        val sortDirection: SortDirection,
    )

    enum class ProductSortOrdering {
        BEST_MATCH,
        PRICE;
    }

    enum class SortDirection {
        ASC,
        DESC;
    }

    suspend fun getProductIdsPaginated(offset: Int, limit: Int) =
        appDatabase.productDao().getProductIdsPaginated(offset, limit)

    fun searchCard(options: ProductSearchOptions) =
        appDatabase.productDao().searchProducts(
            query = options.nameQuery ?: "",
            sortOrdering = options.sortOrdering.ordinal,
            sortDirection = options.sortDirection.ordinal,
            hasProductTypeFilter = options.productTypes.isNotEmpty(),
            productTypes = options.productTypes,
            hasSetFilter = options.setIds.isNotEmpty(),
            setIds = options.setIds,
            hasRarityFilter = options.rarities.isNotEmpty(),
            rarities = options.rarities,
            hasCardTypeFilter = options.cardTypes.isNotEmpty(),
            cardTypes = options.cardTypes,
        )

    suspend fun getProductWithSkusById(id: Long) =
        appDatabase.productDao().getProductWithSkusById(id)

    suspend fun upsertProducts(products: List<Product>) =
        appDatabase.productDao().upsert(products)


    fun getSuggestionsCursorObservable(query: String?, setId: Long? = null) =
        appDatabase.productDao().getSearchSuggestionsObservable(listOf(ProductType.CARD), setId, query ?: "").map {
            it.toSearchSuggestionsCursor()
        }

    fun getProductPrintings(name: String) = appDatabase.productDao().getProductPrintings(name)

    suspend fun getCardCount() = appDatabase.productDao().getProductCount()

    suspend fun getRarities(query: String?) =
        appDatabase.productDao().getRarities(getFuzzySearchQuery(query ?: ""))

    suspend fun updateProductPrices(updates: List<Product.ProductPriceUpdate>) =
        appDatabase.productDao().updateProductPrices(updates)
}
