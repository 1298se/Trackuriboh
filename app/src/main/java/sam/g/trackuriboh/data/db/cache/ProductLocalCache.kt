package sam.g.trackuriboh.data.db.cache

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.map
import sam.g.trackuriboh.data.db.AppDatabase
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
    fun searchCardByName(name: String?): PagingSource<Int, ProductWithCardSetAndSkuIds> =
        searchCardInSetByName(null, name)

    fun searchCardInSetByName(setId: Long?, name: String?): PagingSource<Int, ProductWithCardSetAndSkuIds> =
            appDatabase.productDao().searchProductByName(listOf(ProductType.CARD), setId, name ?: "")

    suspend fun getProductWithSkusById(id: Long) =
        appDatabase.productDao().getProductWithSkusById(id)

    suspend fun insertProducts(products: List<Product>) =
        appDatabase.productDao().insert(products)

    fun getSuggestionsCursorObservable(query: String?, setId: Long? = null) =
        appDatabase.productDao().getSearchSuggestionsObservable(listOf(ProductType.CARD), setId, query ?: "").map {
            it.toSearchSuggestionsCursor()
        }

    fun getProductPrintings(name: String) = appDatabase.productDao().getProductPrintings(name)

    suspend fun getTotalCardCount() = appDatabase.productDao().getTotalCount()
}
