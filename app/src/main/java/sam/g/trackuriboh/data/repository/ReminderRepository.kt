package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.ReminderLocalCache
import sam.g.trackuriboh.data.db.entities.Reminder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val reminderLocalCache: ReminderLocalCache
) {

    suspend fun insertAndReturnReminder(reminder: Reminder) = reminderLocalCache.insertAndReturnReminder(reminder)

    fun getRemindersObservable() = reminderLocalCache.getRemindersObservable()

    suspend fun getReminders(): List<Reminder> = reminderLocalCache.getReminders()

    suspend fun updateReminder(reminder: Reminder) = reminderLocalCache.updateReminder(reminder)

    suspend fun deleteReminder(reminder: Reminder) = reminderLocalCache.deleteReminder(reminder)
}