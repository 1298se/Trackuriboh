package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.types.ProductType

@Dao
interface ProductDao : BaseDao<Product> {
    @Query("SELECT * FROM Product WHERE Product.id = :productId")
    suspend fun getProductWithSkusById(productId: Long): ProductWithCardSetAndSkuIds?

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
    suspend fun getSearchSuggestions(
        productTypes: List<ProductType>,
        setId: Long?,
        query: String,
        fuzzyQuery: String = getFuzzySearchQuery(query),
        limit: Int = SEARCH_SUGGESTIONS_RESULT_SIZE
    ): List<String>

    @Query("DELETE FROM Product")
    suspend fun clearTable()
}
