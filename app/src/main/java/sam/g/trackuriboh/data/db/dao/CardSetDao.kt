package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.CardSet

@Dao
interface CardSetDao : BaseDao<CardSet> {
    @Query("SELECT * FROM CardSet WHERE id = :setId")
    suspend fun getCardSet(setId: Long): CardSet

    @MapInfo(valueColumn = "cardSetCount")
    @Transaction
    @Query("SELECT CardSet.*, COUNT(Product.id) AS cardSetCount FROM CardSet LEFT JOIN Product ON CardSet.id = Product.setId GROUP BY CardSet.id")
    suspend fun getAllCardSetsWithCount(): Map<CardSet, Int>

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
