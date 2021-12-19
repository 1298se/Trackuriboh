package sam.g.trackuriboh.ui_notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.data.repository.ReminderRepository
import sam.g.trackuriboh.data.types.ReminderType
import sam.g.trackuriboh.managers.NotificationsScheduler
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotificationFormViewModel @Inject constructor(
    application: Application,
    private val reminderRepository: ReminderRepository,
    private val notificationsScheduler: NotificationsScheduler
) : AndroidViewModel(application) {
    data class RemindersFormState(
        val reminderTypeOptions: List<ReminderType> = emptyList(),
        val reminderType: ReminderType? = null,
        val link: String? = null,
        val date: Date? = null,
    )

    val formState: LiveData<RemindersFormState>
        get() = _formState

    private val _formState = MutableLiveData(RemindersFormState(reminderTypeOptions = ReminderType.values().toList()))

    fun onLinkValueChanged(link: String) {
        _formState.value = _formState.value?.copy(link = link)
    }

    fun onReminderTypeChanged(position: Int) {
        _formState.value = _formState.value?.let {
            it.copy(reminderType = it.reminderTypeOptions[position])
        }
    }

    fun onDateChanged(date: Date) {
        _formState.value = _formState.value?.copy(date = date)
    }

    fun save() {
        viewModelScope.launch {
            val reminderType = formState.value?.reminderType
            val link = formState.value?.link
            val date = formState.value?.date

            if (reminderType != null && link != null && date != null) {
                val reminderId = reminderRepository.insertReminder(
                    Reminder(type = reminderType, link = link, date = date)
                )

                if (notificationsScheduler.canScheduleReminders()) {
                    notificationsScheduler.setReminder(reminderRepository.getReminder(reminderId))
                } else {

                }
            }
        }
    }
}
