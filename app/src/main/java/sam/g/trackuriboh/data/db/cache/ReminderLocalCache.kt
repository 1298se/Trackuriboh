package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Reminder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    suspend fun insertReminder(reminder: Reminder) = appDatabase.reminderDao().insert(reminder)

    suspend fun getReminder(reminderId: Long) = appDatabase.reminderDao().getReminder(reminderId)
}