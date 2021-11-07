package sam.g.trackuriboh.data.db

import androidx.paging.PagingSource
import sam.g.trackuriboh.data.db.entities.*
import sam.g.trackuriboh.data.db.relations.ProductWithSetInfo
import sam.g.trackuriboh.data.types.ProductType
import javax.inject.Inject

class AppLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun searchCardByName(name: String?): PagingSource<Int, ProductWithSetInfo> {
        val query = "%${(name ?: "").replace(' ', '%')}%"
        return appDatabase.productDao().searchProductByName(query, ProductType.CARD)
    }

    fun searchCardSetByName(name: String?): PagingSource<Int, CardSet> {
        val query = "%${(name ?: "").replace(' ', '%')}%"
        return appDatabase.cardSetDao().searchCardSetByName(query)
    }

    suspend fun insertCardSets(cardSets: List<CardSet>): List<Long> =
        appDatabase.cardSetDao().insertCardSets(cardSets)

    suspend fun insertProducts(products: List<Product>): List<Long> =
        appDatabase.productDao().insertProducts(products)

    suspend fun insertCardRarities(rarities: List<CardRarity>): List<Long> =
        appDatabase.cardRarityDao().insertCardRarities(rarities)

    suspend fun insertConditions(conditions: List<Condition>): List<Long> =
        appDatabase.conditionDao().insertConditions(conditions)

    suspend fun insertPrintings(printings: List<Printing>): List<Long> =
        appDatabase.printingDao().insertPrintings(printings)

    suspend fun insertProductSkus(skus: List<Sku>): List<Long> =
        appDatabase.productSkuDao().insertSkus(skus)
}
