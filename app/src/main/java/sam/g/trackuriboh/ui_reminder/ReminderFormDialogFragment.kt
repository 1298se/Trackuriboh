package sam.g.trackuriboh.ui_reminder


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.DialogRemindersFormBinding
import sam.g.trackuriboh.ui_common.*
import sam.g.trackuriboh.ui_reminder.components.RemindersForm
import sam.g.trackuriboh.utils.viewBinding
import java.util.*

/**
 * Bottom Sheet is kinda buggy with Compose. The cursor keeps jumping everywhere when
 * the keyboard pops up. Guess we'll just change it to a dialog instead...
 */
@ExperimentalMaterialApi
@AndroidEntryPoint
class ReminderFormDialogFragment : DialogFragment(), DateTimePickerView.OnInteractionListener {
    private val binding: DialogRemindersFormBinding by viewBinding(DialogRemindersFormBinding::inflate)
    private val viewModel: ReminderFormViewModel by viewModels()

    companion object {
        const val NOTIFICATION_FORM_DATA_REQUEST_KEY = "NotificationFormFragment_formRequestKey"
        const val NOTIFICATION_FORM_DATA_RESULT = "NotificationFormFragment_formData"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setCanceledOnTouchOutside(false)

        binding.composeContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    RemindersFormContent()
                }
            }
        }

        binding.dateTimePicker.setOnInteractionListener(this@ReminderFormDialogFragment)

        return binding.root
    }

    /**
     * Cancel from DateTimePicker
     */
    override fun onCancelClick() {
        showRemindersForm()
    }

    /**
     * Save from DateTimePicker
     */
    override fun onSaveClick(calendar: Calendar) {
        viewModel.onDateChanged(calendar.time)
        showRemindersForm()
    }

    @Composable
    private fun RemindersFormContent() {
        RemindersForm(
            formState = viewModel.formState,
            onHostChanged = viewModel::onHostChanged,
            onLinkChanged = viewModel::onLinkValueChanged,
            onReminderTypeSelected = viewModel::onReminderTypeChanged,
            showDateTimePicker = ::showDateTimePicker,
            onSaveClick = ::setReminder,
            onCancelClick = ::dismiss,
        )
    }

    @Preview
    @Composable
    fun Preview() {
        MdcTheme {
            RemindersFormContent()
        }
    }

    private fun setReminder() {
        setFragmentResult(
            NOTIFICATION_FORM_DATA_REQUEST_KEY, bundleOf(
                NOTIFICATION_FORM_DATA_RESULT to viewModel.formState.value?.formData?.toDatabaseEntity())
        )
        dismiss()
    }

    private fun showDateTimePicker() {
        binding.composeContainer.visibility = View.GONE
        binding.dateTimePicker.visibility = View.VISIBLE
    }

    private fun showRemindersForm() {
        binding.composeContainer.visibility = View.VISIBLE
        binding.dateTimePicker.visibility = View.GONE
    }
}