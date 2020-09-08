package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.paging.DataSource
import androidx.room.*
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardXCardSetRef
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardSetInfo
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo

@Dao
abstract class CardXCardSetDao : CardDao, CardSetDao {
    @Transaction
    @Query(
        "SELECT Card.cardId, " +
                "Card.name, " +
                "Card.type, " +
                "Card.desc, " +
                "Card.atk, " +
                "Card.def, " +
                "Card.level, " +
                "Card.race, " +
                "Card.attribute, " +
                "Card.archetype, " +
                "Card.scale, " +
                "Card.linkval, " +
                "Card.linkmarkers, " +
                "Card.cardImageList FROM CardXCardSetRef " +
                "INNER JOIN Card ON Card.cardId = CardXCardSetRef.cardId " +
                "WHERE CardXCardSetRef.setCode = :setCode ORDER BY name ASC"
    )
    abstract fun getCardListBySet(setCode: String): DataSource.Factory<Int, Card>

    @Transaction
    @Query(
        "SELECT CardSet.setCode, " +
                "CardSet.setName, " +
                "CardXCardSetRef.cardNumber, " +
                "CardXCardSetRef.rarity, " +
                "CardXCardSetRef.price, " +
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