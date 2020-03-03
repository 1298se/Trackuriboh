package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSetCrossRef

@Dao
abstract class CardSetDao {
    @Insert
    abstract suspend fun insertJoin(join: CardSetCrossRef)
}