package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product
import java.util.Date

@Dao
interface CardSetDao : BaseDao<CardSet> {
    @Query("SELECT * FROM CardSet WHERE id = :setId")
    suspend fun getCardSet(setId: Long): CardSet?

    @Query("SELECT COUNT(*) FROM CardSet")
    suspend fun getTotalCount(): Int

    @MapInfo(valueColumn = "cardSetCount")
    @Transaction
    @Query("SELECT CardSet.*, COUNT(Product.id) AS cardSetCount FROM CardSet LEFT JOIN Product ON Product.setId = CardSet.id GROUP BY CardSet.id")
    suspend fun getAllCardSetsWithCount(): Map<CardSet, Int>

    @Transaction
    @Query(
        "SELECT * FROM (SELECT CardSet.* FROM CardSet " +
                "LEFT JOIN Product ON Product.setId = CardSet.id " +
                "WHERE releaseDate <= :today " +
                "GROUP BY CardSet.id HAVING COUNT(Product.id) > 0 " +
                "ORDER BY CardSet.releaseDate DESC LIMIT :cardSetLimit) AS recentCardSets " +
                "JOIN Product p ON recentCardSets.id = p.setId " +
                "WHERE p.id IN (SELECT id FROM Product p2 WHERE p2.setId = recentCardSets.id ORDER BY marketPrice DESC LIMIT :cardLimit) " +
                "ORDER BY recentCardSets.releaseDate DESC, p.marketPrice DESC;"
    )
    suspend fun getRecentCardSetsAndMostExpensiveCards(
        cardSetLimit: Int,
        cardLimit: Int,
        today: Date = Date()
    ): Map<CardSet, List<Product>>

    @Query("SELECT * FROM CardSet ORDER BY name ASC")
    fun getCardSetList(): PagingSource<Int, CardSet>

    @Query("SELECT CardSet.* FROM CardSet " +
            "LEFT JOIN Product ON CardSet.id = Product.setId " +
            "WHERE CardSet.name LIKE :fuzzyQuery " +
            "GROUP BY CardSet.id " +
            "HAVING COUNT(Product.id) > 0 " +
            "ORDER BY CASE " +
            "WHEN CardSet.name LIKE :query THEN 1 " +
            "WHEN CardSet.name LIKE :query || '%' THEN 2 " +
            "WHEN CardSet.name LIKE '%' || :query || '%' THEN 3 " +
            "WHEN CardSet.name LIKE '%' || :query THEN 4 " +
            "ELSE 5 " +
            "END, INSTR(LOWER(CardSet.name), :query), releaseDate DESC")
    fun searchCardSetByName(query: String, fuzzyQuery: String = getFuzzySearchQuery(query)): PagingSource<Int, CardSet>

    @Query("SELECT DISTINCT name FROM CardSet " +
            "WHERE name LIKE :fuzzyQuery " +
            "ORDER BY CASE " +
            "WHEN name LIKE :query THEN 1 " +
            "WHEN name LIKE :query || '%' THEN 2 " +
            "WHEN name LIKE '%' || :query || '%' THEN 3 " +
            "WHEN name LIKE '%' || :query THEN 4 " +
            "ELSE 5 " +
            "END, INSTR(LOWER(name), :query), name LIMIT :limit")
    fun getSearchSuggestions(
        query: String,
        fuzzyQuery: String = getFuzzySearchQuery(query),
        limit: Int = SEARCH_SUGGESTIONS_RESULT_SIZE
    ): Flow<List<String>>

    @Query("DELETE FROM CardSet")
    suspend fun clearTable()
}
