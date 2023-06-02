package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.CardRarity
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.types.ProductType

@Dao
interface ProductDao : BaseDao<Product> {

    @Query("SELECT id FROM Product LIMIT :limit OFFSET :offset")
    suspend fun getProductIdsPaginated(offset: Int, limit: Int): List<Long>

    @Query("SELECT COUNT(*) FROM Product WHERE type = :productType")
    suspend fun getProductCount(productType: ProductType = ProductType.CARD): Int

    @Transaction
    @Query("SELECT * FROM Product WHERE Product.id = :productId")
    suspend fun getProductWithSkusById(productId: Long): ProductWithCardSetAndSkuIds

    /**
     * For filters, if the list is null then we don't want to apply the filter
     * (i.e. if rarities is null, then get all rarities). However, the autogenerated code doesn't support nullable lists (will
     * crash), so as a workaround we use flags to indicate if we should apply the filter.
     **/
    @Transaction
    @Query(
        "SELECT * FROM Product " +
                "WHERE (name LIKE :fuzzyQuery OR cleanName LIKE :fuzzyQuery OR name LIKE :query) " +
                "AND (:hasProductTypeFilter = 0 OR type IN (:productTypes)) " +
                "AND (:hasSetFilter = 0 OR setId IN (:setIds)) " +
                "AND (:hasRarityFilter = 0 OR rarityId IN (:rarities)) " +
                "AND (:hasCardTypeFilter = 0 OR cardType IN (:cardTypes)) " +
                "ORDER BY " +
                "CASE WHEN :sortOrdering = 1 AND :sortDirection = 0 THEN marketPrice END ASC, " +
                "CASE WHEN :sortOrdering = 1 AND :sortDirection = 1 THEN marketPrice END DESC, " +
                "CASE WHEN :sortOrdering = 0 THEN (CASE " +
                "WHEN name LIKE :query OR cleanName LIKE :query THEN 1 " +
                "WHEN name LIKE :query || '%' OR cleanName LIKE :query || '%' THEN 2 " +
                "WHEN name LIKE '%' || :query || '%' OR cleanName LIKE '%' || :query || '%' THEN 3 " +
                "WHEN name LIKE '%' || :query OR cleanName LIKE '%' || :query THEN 4 " +
                "ELSE 5 " +
                "END) " +
                "END ASC, INSTR(LOWER(name), :query), cleanName "
    )
    fun searchProducts(
        query: String,
        sortOrdering: Int,
        sortDirection: Int,
        hasProductTypeFilter: Boolean = false,
        productTypes: List<ProductType> = emptyList(),
        hasSetFilter: Boolean = false,
        setIds: List<Long> = emptyList(),
        hasRarityFilter: Boolean = false,
        rarities: List<String> = emptyList(),
        hasCardTypeFilter: Boolean = false,
        cardTypes: List<String> = emptyList(),
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

    @Query(
        "SELECT DISTINCT CardRarity.* FROM Product JOIN CardRarity ON CardRarity.id = rarityId " +
                "WHERE (Product.name LIKE :fuzzyQuery OR Product.cleanName LIKE :fuzzyQuery) " +
                "AND CardRarity.id IS NOT NULL " +
                "AND CardRarity.name != 'Unconfirmed' " +
                "ORDER BY CardRarity.id "
    )
    suspend fun getRarities(fuzzyQuery: String): List<CardRarity>

    @Update(entity = Product::class)
    suspend fun updateProductPrices(productPriceUpdates: List<Product.ProductPriceUpdate>)

    @Query("DELETE FROM Product")
    suspend fun clearTable()
}
