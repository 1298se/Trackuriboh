package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Reminder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    suspend fun insertAndReturnReminder(reminder: Reminder) = appDatabase.reminderDao().insertAndReturn(reminder)

    suspend fun getReminders() = appDatabase.reminderDao().getReminders()

    suspend fun deleteReminder(reminder: Reminder) = appDatabase.reminderDao().delete(reminder)

    fun getRemindersObservable() = appDatabase.reminderDao().getRemindersObservable()

    suspend fun updateReminder(reminder: Reminder) = appDatabase.reminderDao().update(reminder)

}