package sam.g.trackuriboh.ui.reminder

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.data.types.ReminderType
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReminderFormViewModel @Inject constructor(
    savedState: SavedStateHandle,
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

    data class ReminderFormData(
        val reminderType: ReminderType? = null,
        val host: String? = null,
        val link: String? = null,
        val date: Date? = null,
    ) : AppDatabase.DatabaseEntity<Reminder> {

        override fun toDatabaseEntity(): Reminder =
            Reminder(type = reminderType!!,  date = date!!, link = link!!.trim(), host = host?.trim())
    }

    // TODO: Fix Magic Strings
    private val reminder = savedState.get<Reminder?>("reminder")

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
}
