package sam.g.trackuriboh.data.db

import androidx.paging.PagingSource
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
        appDatabase.productDao().searchProductByName(ProductType.CARD, toFuzzySearchQuery(name))

    fun searchCardInSetByName(setId: Long?, name: String?): PagingSource<Int, ProductWithCardSetAndSkuIds> {
        return if (setId == null) {
            searchCardByName(name)
        } else {
            appDatabase.productDao().searchProductInSetByName(setId, toFuzzySearchQuery(name))
        }
    }

    fun searchCardSetByName(name: String?): PagingSource<Int, CardSet> =
        appDatabase.cardSetDao().searchCardSetByName(toFuzzySearchQuery(name))

    suspend fun getCardSet(setId: Long) =
        appDatabase.cardSetDao().getCardSet(setId)

    suspend fun getProductWithSkusById(id: Long) =
        appDatabase.productDao().getProductWithSkusById(id)

    suspend fun insertCardSets(cardSets: List<CardSet>) =
        appDatabase.cardSetDao().insertCardSets(cardSets)

    suspend fun insertProducts(products: List<Product>) =
        appDatabase.productDao().insertProducts(products)

    suspend fun insertCardRarities(rarities: List<CardRarity>) =
        appDatabase.cardRarityDao().insertCardRarities(rarities)

    suspend fun insertConditions(conditions: List<Condition>) =
        appDatabase.conditionDao().insertConditions(conditions)

    suspend fun insertPrintings(printings: List<Printing>) =
        appDatabase.printingDao().insertPrintings(printings)

    suspend fun insertProductSkus(skus: List<Sku>) =
        appDatabase.skuDao().insertSkus(skus)

    suspend fun updateSkuPrices(skuPriceUpdates: List<Sku.SkuPriceUpdate>) =
        appDatabase.skuDao().updateSkuPrices(*skuPriceUpdates.toTypedArray())

    suspend fun getSkusWithConditionAndPrinting(skuIds: List<Long>) =
        appDatabase.skuDao().getSkusWithConditionAndPrinting(skuIds)

    private fun toFuzzySearchQuery(query: String?) = "%${(query ?: "").replace(' ', '%')}%"
}
