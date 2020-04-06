package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.room.*
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardXCardSetRef
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardSetInfo
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo

@Dao
abstract class CardXCardSetDao : CardDao(), CardSetDao {
    @Query(
        "SELECT " +
                "CardSet.setCode, " +
                "CardSet.setName, " +
                "CardXCardSetRef.rarity, " +
                "CardSet.releaseDate FROM CardXCardSetRef " +
                "INNER JOIN CardSet ON CardSet.setCode = CardXCardSetRef.setCode " +
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertJoins(joins: List<CardXCardSetRef>): List<Long>
}