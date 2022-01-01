package sam.g.trackuriboh.data.db.cache

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.map
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.dao.toSearchSuggestionsCursor
import sam.g.trackuriboh.data.db.entities.CardRarity
import sam.g.trackuriboh.data.db.entities.Condition
import sam.g.trackuriboh.data.db.entities.Printing
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

    suspend fun insertCardRarities(rarities: List<CardRarity>) =
        appDatabase.cardRarityDao().insert(rarities)

    suspend fun insertConditions(conditions: List<Condition>) =
        appDatabase.conditionDao().insert(conditions)

    suspend fun insertPrintings(printings: List<Printing>) =
        appDatabase.printingDao().insert(printings)

    fun getSuggestionsCursorObservable(query: String?, setId: Long? = null) =
        appDatabase.productDao().getSearchSuggestionsObservable(listOf(ProductType.CARD), setId, query ?: "").map {
            it.toSearchSuggestionsCursor()
        }

    fun getProductPrintings(name: String) = appDatabase.productDao().getProductPrintings(name)
}
