package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.room.*
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSetXRef
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardSetInfo
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo

@Dao
abstract class CardXCardSetDao : CardDao(), CardSetDao {
    @Query(
        "SELECT " +
                "CardSet.setCode, " +
                "CardSet.setName, " +
                "CardSetXRef.rarity, " +
                "CardSet.releaseDate FROM CardSetXRef " +
                "INNER JOIN CardSet ON CardSet.setCode = CardSetXRef.setCode " +
                "WHERE CardSetXRef.cardId = :cardId"
    )
    abstract suspend fun getCardSetInfo(cardId: Long): List<CardSetInfo>

    @Transaction
    open suspend fun getCardWithSetInfo(cardId: Long): CardWithSetInfo {
        val card = getCard(cardId)
        val setInfoList = getCardSetInfo(cardId)

        return CardWithSetInfo(card, setInfoList)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertJoin(join: CardSetXRef): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertJoins(joins: List<CardSetXRef>): List<Long>
}