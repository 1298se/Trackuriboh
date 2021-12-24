package sam.g.trackuriboh.ui_reminder

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.R
import sam.g.trackuriboh.actions.RequestPermission
import sam.g.trackuriboh.actions.UiAction
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.data.repository.ReminderRepository
import sam.g.trackuriboh.managers.ReminderScheduler
import sam.g.trackuriboh.utils.SingleEvent
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler,
    private val application: Application
) : ViewModel() {

    sealed class UiModel {
        data class ReminderItem(val reminder: Reminder) : UiModel()
        data class Header(val title: String) : UiModel()
    }

    val action: LiveData<SingleEvent<UiAction>>
        get() = _action

    private val _action = MutableLiveData<SingleEvent<UiAction>>()

    val reminders = Transformations.map(reminderRepository.getRemindersFlow().asLiveData()) {
        createRemindersList(it)
    }

    @SuppressLint("InlinedApi")
    fun save(reminder: Reminder) {
        viewModelScope.launch {
            val reminderId = reminderRepository.insertReminder(reminder)

            if (reminderScheduler.canScheduleReminders()) {
                reminderScheduler.setReminder(reminderRepository.getReminder(reminderId))
            } else {
                // Ignore lint because canScheduleReminders checks for API version compatibility
                _action.value = SingleEvent(RequestPermission(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminder)
        }
    }

    private fun createRemindersList(reminders: List<Reminder>?): List<UiModel>? {
        if (reminders == null) {
            return null
        }

        val upcoming = ArrayList<UiModel.ReminderItem>()
        val past = ArrayList<UiModel.ReminderItem>()
        val curDate = Date()

        reminders.forEach {
            if (it.date > curDate) {
                upcoming.add(UiModel.ReminderItem(it))
            } else {
                past.add(UiModel.ReminderItem(it))
            }
        }

        // For upcoming we want earliest to latest
        upcoming.sortBy { it.reminder.date }

        // For past we want most recent to least recent
        past.sortByDescending { it.reminder.date }

        val total = ArrayList<UiModel>()

        if (upcoming.size > 0) {
            total.add(UiModel.Header(application.getString(R.string.lbl_upcoming)))
            total.addAll(upcoming)
        }

        if (past.size > 0) {
            total.add(UiModel.Header(application.getString(R.string.lbl_earlier)))
            total.addAll(past)
        }

        return total
    }
}