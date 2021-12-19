package sam.g.trackuriboh.data.db.cache

import android.app.SearchManager
import android.database.MatrixCursor
import android.provider.BaseColumns
import androidx.paging.PagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.g.trackuriboh.data.db.AppDatabase
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
        appDatabase.productDao().searchProductByName(listOf(ProductType.CARD), name ?: "")

    fun searchCardInSetByName(setId: Long?, name: String?): PagingSource<Int, ProductWithCardSetAndSkuIds> {
        return if (setId == null) {
            searchCardByName(name)
        } else {
            appDatabase.productDao().searchProductInSetByName(setId, name ?: "")
        }
    }

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

    suspend fun getSearchSuggestions(query: String?) = withContext(Dispatchers.IO) {
        return@withContext MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1)).apply {
            appDatabase.productDao().getSearchSuggestions(listOf(ProductType.CARD), query ?: "").forEachIndexed { index, s ->
                addRow(arrayOf(index, s))
            }
        }
    }
}
