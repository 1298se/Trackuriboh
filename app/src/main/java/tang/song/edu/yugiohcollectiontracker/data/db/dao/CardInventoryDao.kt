package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory

@Dao
interface CardInventoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventory(card: CardInventory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryList(card: List<CardInventory>): List<Long>
}
