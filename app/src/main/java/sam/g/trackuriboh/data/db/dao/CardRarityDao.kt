package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.CardRarity

@Dao
interface CardRarityDao : BaseDao<CardRarity> {
    @Query("DELETE FROM CardRarity")
    suspend fun clearTable()
}
