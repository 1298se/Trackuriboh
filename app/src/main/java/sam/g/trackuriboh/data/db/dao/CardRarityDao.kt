package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import sam.g.trackuriboh.data.db.entities.CardRarity

@Dao
interface CardRarityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardRarities(rarities: List<CardRarity>): List<Long>
}
