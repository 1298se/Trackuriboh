package sam.g.trackuriboh.data.db.cache

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.map
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.dao.getFuzzySearchQuery
import sam.g.trackuriboh.data.db.dao.toSearchSuggestionsCursor
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.types.ProductType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    data class ProductSearchManifest(
        val nameQuery: String? = null,
        val productTypes: List<ProductType> = emptyList(),
        val setIds: List<Long> = emptyList(),
        val rarities: List<String> = emptyList(),
        val cardTypes: List<String> = emptyList(),
    )

    fun searchCardByName(name: String?): PagingSource<Int, ProductWithCardSetAndSkuIds> =
        searchCard(ProductSearchManifest(nameQuery = name))

    fun searchCardInSetByName(setId: Long, name: String?): PagingSource<Int, ProductWithCardSetAndSkuIds> =
        searchCard(ProductSearchManifest(nameQuery = name, setIds = listOf(setId)))

    private fun searchCard(filters: ProductSearchManifest): PagingSource<Int, ProductWithCardSetAndSkuIds> {
        return appDatabase.productDao().searchProducts(
            query = filters.nameQuery ?: "",
            hasProductTypeFilter = filters.productTypes.isNotEmpty(),
            productTypes = filters.productTypes,
            hasSetFilter = filters.setIds.isNotEmpty(),
            setIds = filters.setIds,
            hasRarityFilter = filters.rarities.isNotEmpty(),
            rarities = filters.rarities,
            hasCardTypeFilter = filters.cardTypes.isNotEmpty(),
            cardTypes = filters.cardTypes,
        )
    }

    suspend fun getProductWithSkusById(id: Long) =
        appDatabase.productDao().getProductWithSkusById(id)

    suspend fun upsertProducts(products: List<Product>) =
        appDatabase.productDao().upsert(products)


    fun getSuggestionsCursorObservable(query: String?, setId: Long? = null) =
        appDatabase.productDao().getSearchSuggestionsObservable(listOf(ProductType.CARD), setId, query ?: "").map {
            it.toSearchSuggestionsCursor()
        }

    fun getProductPrintings(name: String) = appDatabase.productDao().getProductPrintings(name)

    suspend fun getTotalCardCount() = appDatabase.productDao().getTotalCount()

    suspend fun getRarities(query: String?) =
        appDatabase.productDao().getRarities(getFuzzySearchQuery(query ?: ""))

}
