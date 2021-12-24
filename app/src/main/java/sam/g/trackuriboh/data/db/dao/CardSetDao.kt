package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.MapInfo
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.CardSet

@Dao
interface CardSetDao : BaseDao<CardSet> {
    @Query("SELECT * FROM CardSet WHERE id = :setId")
    suspend fun getCardSet(setId: Long): CardSet

    @MapInfo(valueColumn = "cardSetCount")
    @Query("SELECT CardSet.id, CardSet.code, CardSet.name, CardSet.releaseDate, COUNT(Product.id) AS cardSetCount FROM CardSet LEFT JOIN Product ON CardSet.id = Product.setId GROUP BY CardSet.id")
    suspend fun getCardSetsWithCount(): Map<CardSet, Int>

    @Query("SELECT * FROM CardSet ORDER BY name ASC")
    fun getCardSetList(): PagingSource<Int, CardSet>

    @Query("SELECT * FROM CardSet " +
            "WHERE name LIKE :fuzzyQuery " +
            "ORDER BY CASE " +
            "WHEN name LIKE :query THEN 1 " +
            "WHEN name LIKE :query || '%' THEN 2 " +
            "WHEN name LIKE '%' || :query || '%' THEN 3 " +
            "WHEN name LIKE '%' || :query THEN 4 " +
            "ELSE 5 " +
            "END, INSTR(LOWER(name), :query), name")
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
    suspend fun getSearchSuggestions(
        query: String,
        fuzzyQuery: String = getFuzzySearchQuery(query),
        limit: Int = SEARCH_SUGGESTIONS_RESULT_SIZE
    ): List<String>

    @Query("DELETE FROM CardSet")
    suspend fun clearTable()
}
