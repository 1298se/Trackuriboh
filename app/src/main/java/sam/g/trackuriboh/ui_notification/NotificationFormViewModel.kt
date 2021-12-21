package sam.g.trackuriboh.ui_notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.types.ReminderType
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotificationFormViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    data class RemindersFormState(
        val reminderTypeOptions: List<ReminderType> = emptyList(),
        val canSave: Boolean = false,
        val form: RemindersForm = RemindersForm()
    )

    data class RemindersForm(
        val reminderType: ReminderType? = null,
        val link: String? = null,
        val date: Date? = null,
    )

    private val _formState = MutableLiveData(RemindersFormState(reminderTypeOptions = ReminderType.values().toList()))

    val formState = Transformations.map(_formState) {
        if (validate(it.form)) {
            it.copy(canSave = true)
        } else {
            it.copy(canSave = false)
        }
    }


    fun onLinkValueChanged(link: String) {
        _formState.value = _formState.value?.let {
            it.copy(form = it.form.copy(link = link))
        }
    }

    fun onReminderTypeChanged(position: Int) {
        _formState.value = _formState.value?.let {
            it.copy(form = it.form.copy(reminderType = it.reminderTypeOptions[position]))
        }
    }

    fun onDateChanged(date: Date) {
        _formState.value = _formState.value?.let {
           it.copy(form = it.form.copy(date = date))
        }
    }


    private fun validate(formState: RemindersForm): Boolean {
        return formState.date != null && !formState.link.isNullOrBlank() && formState.reminderType != null
    }
}
