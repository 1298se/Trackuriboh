package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.Reminder

@Dao
interface ReminderDao : BaseDao<Reminder> {

    @Query("SELECT * FROM Reminder WHERE id = :reminderId")
    suspend fun getReminder(reminderId: Long): Reminder

    @Query("DELETE FROM Reminder")
    suspend fun clearTable()
}
