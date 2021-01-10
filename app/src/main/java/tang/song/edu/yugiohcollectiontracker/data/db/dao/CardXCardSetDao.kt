package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardXCardSetRef
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardSetInfo
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo

@Dao
abstract class CardXCardSetDao : CardDao, CardSetDao {
    @Transaction
    @Query(
        "SELECT * FROM CardXCardSetRef " +
                "INNER JOIN Card ON Card.cardId = CardXCardSetRef.cardId " +
                "WHERE CardXCardSetRef.setName = :setName " +
                "GROUP BY CardXCardSetRef.cardId " +
                "ORDER BY Card.name ASC"
    )
    abstract fun getCardListBySet(setName: String): PagingSource<Int, Card>

    @Transaction
    @Query(
        "SELECT * FROM CardXCardSetRef " +
                "INNER JOIN CardSet ON CardSet.setName = CardXCardSetRef.setName " +
                "WHERE CardXCardSetRef.cardId = :cardId"
    )
    abstract suspend fun getCardSetInfo(cardId: Long): List<CardSetInfo>

    @Transaction
    open suspend fun getCardWithSetInfo(cardId: Long): CardWithSetInfo {
        val card = getCardById(cardId)
        val setInfoList = getCardSetInfo(cardId)

        return CardWithSetInfo(card, setInfoList)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertJoin(join: CardXCardSetRef): Long

    /** Handles case where card set has correct name but different capitalization from CardSet table**/
    open suspend fun fuzzyInsertJoin(join: CardXCardSetRef): Long {
        // Try to find a set with a similar lane
        val fuzzySearchResults = _searchCardSet("%${join.setName}%")

        // If the setcode matches, update the name
        if (fuzzySearchResults.size == 1 && parseCardSetCode(join.cardNumber) == fuzzySearchResults[0].setCode) {
            join.setName = fuzzySearchResults[0].setName
        }

        return insertJoin(join)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertJoins(joins: List<CardXCardSetRef>): List<Long>

    private fun parseCardSetCode(cardNumber: String): String? {
        val hyphenIndex = cardNumber.indexOf('-')
        return if (hyphenIndex != -1) cardNumber.substring(0, hyphenIndex) else null
    }

}