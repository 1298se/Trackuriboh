package sam.g.trackuriboh.data.db

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.*
import sam.g.trackuriboh.data.db.relations.ProductWithSetAndSkuIds
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.types.ProductType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun searchCardByName(name: String?): PagingSource<Int, ProductWithSetAndSkuIds> {
        val query = "%${(name ?: "").replace(' ', '%')}%"
        return appDatabase.productDao().searchProductByName(query, ProductType.CARD)
    }

    fun searchCardSetByName(name: String?): PagingSource<Int, CardSet> {
        val query = "%${(name ?: "").replace(' ', '%')}%"
        return appDatabase.cardSetDao().searchCardSetByName(query)
    }

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

    fun getSkusWithConditionAndPrinting(
        skuIds: List<Long>
    ): Flow<List<SkuWithConditionAndPrinting>> =
        appDatabase.skuDao().getSkusWithConditionAndPrinting(skuIds)
}
