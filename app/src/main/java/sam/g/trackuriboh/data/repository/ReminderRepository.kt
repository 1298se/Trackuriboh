package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.ReminderLocalCache
import sam.g.trackuriboh.data.db.entities.Reminder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val reminderLocalCache: ReminderLocalCache
) {

    suspend fun insertReminder(reminder: Reminder) = reminderLocalCache.insertReminder(reminder)

    suspend fun getReminder(reminderId: Long) = reminderLocalCache.getReminder(reminderId)

    fun getRemindersFlow() = reminderLocalCache.getRemindersFlow()

    suspend fun getReminders(): List<Reminder> = reminderLocalCache.getReminders()

    suspend fun deleteReminder(reminder: Reminder) = reminderLocalCache.deleteReminder(reminder)
}