package sam.g.trackuriboh.data.db.cache

import androidx.paging.PagingSource
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.dao.toSearchSuggestionsCursor
import sam.g.trackuriboh.data.db.entities.*
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

    suspend fun insertSkus(skus: List<Sku>) =
        appDatabase.skuDao().insert(skus)

    suspend fun updateSkuPrices(skuPriceUpdates: List<Sku.SkuPriceUpdate>) =
        appDatabase.skuDao().updateSkuPrices(skuPriceUpdates)

    suspend fun getSkusWithConditionAndPrinting(skuIds: List<Long>) =
        appDatabase.skuDao().getSkusWithConditionAndPrinting(skuIds)

    suspend fun getSearchSuggestions(query: String?) =
        getSearchSuggestionsInSet(null, query)

    suspend fun getSearchSuggestionsInSet(setId: Long?, query: String?) =
        appDatabase.productDao().getSearchSuggestions(listOf(ProductType.CARD), setId, query ?: "")
            .toSearchSuggestionsCursor()
}
