package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.CardInventory
import sam.g.trackuriboh.data.types.EditionType

@Dao
interface CardInventoryDao {
    @Query("SELECT * FROM CardInventory ORDER BY lastTransaction DESC")
    fun getInventoryList(): PagingSource<Int, CardInventory>

    @Query("SELECT * FROM CardInventory WHERE cardNumber = :cardNumber AND rarity = :rarity AND edition = :edition")
    suspend fun getInventory(cardNumber: String, rarity: String?, edition: EditionType?): CardInventory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventory(card: CardInventory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryList(card: List<CardInventory>): List<Long>
}
