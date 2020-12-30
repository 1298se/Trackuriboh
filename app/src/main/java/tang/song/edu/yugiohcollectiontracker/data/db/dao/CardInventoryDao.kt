package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory

@Dao
interface CardInventoryDao {
    @Query("SELECT * FROM CardInventory WHERE cardNumber = :cardNumber AND rarity = :rarity")
    suspend fun getInventory(cardNumber: String, rarity: String): CardInventory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventory(card: CardInventory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryList(card: List<CardInventory>): List<Long>
}
