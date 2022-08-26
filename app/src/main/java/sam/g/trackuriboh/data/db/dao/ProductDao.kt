package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.types.ProductType

@Dao
interface ProductDao : BaseDao<Product> {

    @Query("SELECT COUNT(*) FROM Product WHERE type = :productType")
    suspend fun getTotalCount(productType: ProductType = ProductType.CARD): Int

    @Transaction
    @Query("SELECT * FROM Product WHERE Product.id = :productId")
    suspend fun getProductWithSkusById(productId: Long): ProductWithCardSetAndSkuIds

    @Transaction
    @MapInfo(valueColumn = "lowestListingPrice")
    @Query("SELECT Product.*, MIN(Sku.lowestListingPrice) AS lowestListingPrice FROM Product " +
            "LEFT JOIN Sku ON Sku.productId = Product.id " +
            "WHERE Product.setId = :setId " +
            "GROUP BY Product.id " +
            "ORDER BY lowestListingPrice DESC " +
            "LIMIT :limit"
    )
    suspend fun getMostExpensiveProductsInSet(setId: Long, limit: Int): Map<Product, Double?>

    @Transaction
    @Query("SELECT * FROM Product " +
            "WHERE (name LIKE :fuzzyQuery OR cleanName LIKE :fuzzyQuery) " +
            "AND type IN (:productTypes) " +
            "AND (setId = :setId OR :setId IS NULL)" +
            "ORDER BY CASE " +
            "WHEN name LIKE :query OR cleanName LIKE :query THEN 1 " +
            "WHEN name LIKE :query || '%' OR cleanName LIKE :query || '%' THEN 2 " +
            "WHEN name LIKE '%' || :query || '%' OR cleanName LIKE '%' || :query || '%' THEN 3 " +
            "WHEN name LIKE '%' || :query OR cleanName LIKE '%' || :query THEN 4 " +
            "ELSE 5 " +
            "END, INSTR(LOWER(name), :query), cleanName")
    fun searchProductByName(
        productTypes: List<ProductType>,
        setId: Long?,
        query: String,
        fuzzyQuery: String = getFuzzySearchQuery(query)
    ): PagingSource<Int, ProductWithCardSetAndSkuIds>

    @Transaction
    @Query("SELECT DISTINCT name FROM Product " +
            "WHERE (name LIKE :fuzzyQuery OR cleanName LIKE :fuzzyQuery) " +
            "AND type IN (:productTypes) " +
            "AND (setId = :setId OR :setId IS NULL)" +
            "ORDER BY CASE " +
            "WHEN name LIKE :query OR cleanName LIKE :query THEN 1 " +
            "WHEN name LIKE :query || '%' OR cleanName LIKE :query || '%' THEN 2 " +
            "WHEN name LIKE '%' || :query || '%' OR cleanName LIKE '%' || :query || '%' THEN 3 " +
            "WHEN name LIKE '%' || :query OR cleanName LIKE '%' || :query THEN 4 " +
            "ELSE 5 " +
            "END, INSTR(LOWER(name), :query), cleanName LIMIT :limit")
    fun getSearchSuggestionsObservable(
        productTypes: List<ProductType>,
        setId: Long?,
        query: String,
        fuzzyQuery: String = getFuzzySearchQuery(query),
        limit: Int = SEARCH_SUGGESTIONS_RESULT_SIZE
    ): Flow<List<String>>

    @Transaction
    @Query("SELECT * FROM PRODUCT WHERE name = :name ORDER BY name ASC")
    fun getProductPrintings(name: String): PagingSource<Int, ProductWithCardSetAndSkuIds>

    @Query("DELETE FROM Product")
    suspend fun clearTable()
}
