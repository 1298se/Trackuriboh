package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.Reminder

@Dao
interface ReminderDao : BaseDao<Reminder> {

    @Query("SELECT * FROM Reminder WHERE id = :reminderId")
    suspend fun getReminder(reminderId: Long): Reminder

    @Query("DELETE FROM Reminder")
    suspend fun clearTable()

    @Query("SELECT * FROM Reminder")
    suspend fun getReminders(): List<Reminder>

    @Query("SELECT * FROM Reminder")
    fun getRemindersObservable(): Flow<List<Reminder>>
}
