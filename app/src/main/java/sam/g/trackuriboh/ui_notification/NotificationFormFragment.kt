package sam.g.trackuriboh.ui_notification


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.FragmentNotificationFormBinding
import sam.g.trackuriboh.ui_common.*
import sam.g.trackuriboh.ui_notification.components.NotificationForm
import sam.g.trackuriboh.utils.viewBinding
import java.util.*

/**
 * Bottom Sheet is kinda buggy with Compose. The cursor keeps jumping everywhere when
 * the keyboard pops up. Guess we'll just change it to a dialog instead...
 */
@ExperimentalMaterialApi
@AndroidEntryPoint
class NotificationFormFragment : DialogFragment(), DateTimePickerView.OnInteractionListener {
    private val binding: FragmentNotificationFormBinding by viewBinding(FragmentNotificationFormBinding::inflate)
    private val viewModel: NotificationFormViewModel by viewModels()

    companion object {
        const val NOTIFICATION_FORM_DATA_REQUEST_KEY = "NotificationFormFragment_formRequestKey"
        const val NOTIFICATION_FORM_DATA_RESULT = "NotificationFormFragment_formData"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.composeContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    RemindersFormContent()
                }
            }
        }

        binding.dateTimePicker.setOnInteractionListener(this@NotificationFormFragment)

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
        NotificationForm(
            formState = viewModel.formState,
            onLinkValueChanged = viewModel::onLinkValueChanged,
            onReminderTypeSelected = viewModel::onReminderTypeChanged,
            showDateTimePicker = ::showDateTimePicker,
            onSaveClick = ::setReminder,
            onCancelClick = ::dismiss,
        )
    }

    @Preview
    @Composable
    private fun Preview() {
        MdcTheme {
            RemindersFormContent()
        }
    }

    private fun setReminder() {

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