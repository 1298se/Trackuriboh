package sam.g.trackuriboh.ui.reminder

import android.os.Parcelable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.data.types.ReminderType
import java.util.*
import javax.inject.Inject

@ExperimentalMaterialApi
@HiltViewModel
class ReminderFormViewModel @Inject constructor(
    state: SavedStateHandle,
) : ViewModel() {

    data class ReminderFormState(
        val reminderTypeOptions: List<ReminderType> = emptyList(),
        val canSave: Boolean = false,
        val formData: ReminderFormData,
        val mode: Mode
    )

    @Parcelize
    enum class Mode : Parcelable {
        EDIT,
        CREATE
    }

    private val reminder = state.get<Reminder?>(AddEditReminderDialogFragment.ARG_REMINDER)

    data class ReminderFormData(
        val reminderType: ReminderType? = null,
        val host: String? = null,
        val link: String? = null,
        val date: Date? = null,
    )

    private val _formState = MutableLiveData(ReminderFormState(
        mode = if (reminder == null) Mode.CREATE else Mode.EDIT,
        reminderTypeOptions = ReminderType.values().toList(),
        formData = reminder.toFormData()
    ))

    val formState = Transformations.map(_formState) {
        if (validate(it.formData)) {
            it.copy(canSave = true)
        } else {
            it.copy(canSave = false)
        }
    }

    fun createDataBundle() = liveData {
        emit(
            bundleOf(
                AddEditReminderDialogFragment.REMINDER_DATA_KEY to _formState.value?.formData?.toReminder(),
                AddEditReminderDialogFragment.MODE_DATA_KEY to _formState.value?.mode
            )
        )
    }


    fun onLinkValueChanged(link: String) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(link = link))
        }
    }

    fun onReminderTypeChanged(position: Int) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(reminderType = it.reminderTypeOptions[position]))
        }
    }

    fun onDateChanged(date: Date) {
        _formState.value = _formState.value?.let {
           it.copy(formData = it.formData.copy(date = date))
        }
    }

    fun onHostChanged(host: String) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(host = host))
        }
    }

    private fun validate(formDataState: ReminderFormData): Boolean {
        with (formDataState) {
            return date != null &&
                    !link.isNullOrBlank() &&
                    (link.startsWith("http://") or link.startsWith("https://")) &&
                    reminderType != null
        }
    }

    private fun Reminder?.toFormData(): ReminderFormData {
        return this?.let {
            ReminderFormData(
                reminderType = it.type,
                host = it.host,
                link = it.link,
                date = it.date
            )
        } ?: ReminderFormData()
    }

    private fun ReminderFormData.toReminder() =
        Reminder(
            id = reminder?.id ?: 0,
            type = reminderType!!,
            date = date!!,
            link = link!!.trim(),
            host = host?.trim()
        )
}
