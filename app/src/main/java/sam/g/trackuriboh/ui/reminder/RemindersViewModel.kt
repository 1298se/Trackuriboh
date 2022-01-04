package sam.g.trackuriboh.ui.reminder

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sam.g.trackuriboh.R
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.data.repository.ReminderRepository
import sam.g.trackuriboh.managers.ReminderScheduler
import sam.g.trackuriboh.ui.common.actions.RequestPermission
import sam.g.trackuriboh.ui.common.actions.UiAction
import sam.g.trackuriboh.utils.SingleEvent
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@ExperimentalMaterialApi
@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler,
    private val application: Application,
    private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {

    sealed class UiModel {
        data class ReminderItem(val reminder: Reminder) : UiModel()
        data class Header(val title: String) : UiModel()
    }

    val action: LiveData<SingleEvent<UiAction>>
        get() = _action

    private val _action = MutableLiveData<SingleEvent<UiAction>>()

    val reminders = reminderRepository.getRemindersObservable().map { createRemindersList(it) }.asLiveData()

    @SuppressLint("InlinedApi")
    fun save(reminder: Reminder, mode: ReminderFormViewModel.Mode) {

        var scheduleReminder = reminder
        viewModelScope.launch {
            when (mode) {
                ReminderFormViewModel.Mode.EDIT -> {
                    reminderRepository.updateReminder(reminder)

                    firebaseAnalytics.logEvent(Events.EDIT_REMINDER, bundleOf("reminder" to reminder))

                }
                ReminderFormViewModel.Mode.CREATE -> {
                    // Since it's a new reminder, it's id is 0, we need to insert it to get the actual id.
                    scheduleReminder = reminderRepository.insertAndReturnReminder(reminder)

                    firebaseAnalytics.logEvent(Events.CREATE_REMINDER, bundleOf("reminder" to reminder))
                }
            }

            if (reminderScheduler.canScheduleReminders()) {
                reminderScheduler.scheduleReminder(scheduleReminder)
            } else {
                // Ignore lint because canScheduleReminders checks for API version compatibility
                _action.value = SingleEvent(RequestPermission(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
        }
    }

    fun deleteReminder(reminder: Reminder) {
        firebaseAnalytics.logEvent(Events.DELETE_REMINDER, bundleOf("reminder" to reminder))

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