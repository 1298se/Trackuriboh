package sam.g.trackuriboh.ui.reminder


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.DialogAddEditRemindersBinding
import sam.g.trackuriboh.ui.common.*
import sam.g.trackuriboh.ui.reminder.components.ReminderForm
import sam.g.trackuriboh.utils.viewBinding
import java.util.*

/**
 * Bottom Sheet is kinda buggy with Compose. The cursor keeps jumping everywhere when
 * the keyboard pops up. Guess we'll just change it to a dialog instead...
 */
@ExperimentalMaterialApi
@AndroidEntryPoint
class AddEditReminderDialogFragment : DialogFragment(), DateTimePickerView.OnInteractionListener {
    private val binding: DialogAddEditRemindersBinding by viewBinding(DialogAddEditRemindersBinding::inflate)
    private val viewModel: ReminderFormViewModel by viewModels()

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "AddEditReminderDialogFragment_fragmentResultRequestKey"
        const val REMINDER_FORM_DATA_KEY = "AddEditReminderDialogFragment_reminderFormData"
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
                    val formState by viewModel.formState.observeAsState()

                    ReminderForm(
                        state = formState,
                        onHostChanged = viewModel::onHostChanged,
                        onLinkChanged = viewModel::onLinkValueChanged,
                        onReminderTypeSelected = viewModel::onReminderTypeChanged,
                        showDateTimePicker = ::showDateTimePicker,
                        onSaveClick = ::setReminder,
                        onCancelClick = findNavController()::popBackStack,
                    )
                }
            }
        }

        binding.dateTimePicker.setOnInteractionListener(this@AddEditReminderDialogFragment)

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

    @Preview
    @Composable
    fun Preview() {
        MdcTheme {
            ReminderForm(
                state = null,
                onHostChanged = viewModel::onHostChanged,
                onLinkChanged = viewModel::onLinkValueChanged,
                onReminderTypeSelected = viewModel::onReminderTypeChanged,
                showDateTimePicker = ::showDateTimePicker,
                onSaveClick = ::setReminder,
                onCancelClick = findNavController()::popBackStack,
            )
        }
    }

    private fun setReminder() {
        setFragmentResult(
            FRAGMENT_RESULT_REQUEST_KEY, bundleOf(
                REMINDER_FORM_DATA_KEY to viewModel.formState.value?.formData?.toDatabaseEntity())
        )
        findNavController().popBackStack()
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
