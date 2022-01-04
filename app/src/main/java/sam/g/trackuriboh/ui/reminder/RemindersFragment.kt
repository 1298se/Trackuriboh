package sam.g.trackuriboh.ui.reminder

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.databinding.FragmentRemindersBinding
import sam.g.trackuriboh.ui.common.VerticalSpaceItemDecoration
import sam.g.trackuriboh.ui.common.actions.RequestPermission
import sam.g.trackuriboh.ui.reminder.adapters.RemindersAdapter
import sam.g.trackuriboh.utils.*
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class RemindersFragment : Fragment(), RemindersAdapter.OnItemClickListener {

    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics

    private val binding: FragmentRemindersBinding by viewBinding(FragmentRemindersBinding::inflate)

    companion object {
        fun newInstance() = RemindersFragment()
    }

    private val viewModel: RemindersViewModel by viewModels()

    private lateinit var remindersAdapter: RemindersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initFab()
        initFragmentResultListeners()
        initRecyclerView()
        initObservers()
    }

    override fun onItemClick(reminder: Reminder) {
        try {
            startActivity(getOpenLinkIntent(reminder.link))
        } catch (e: ActivityNotFoundException) {
            showSnackbar(getString(R.string.reminder_open_fail_message), SnackbarType.ERROR, binding.remindersFab)
        }
    }

    override fun onItemEditClick(reminder: Reminder) {
        AddEditReminderDialogFragment.newInstance(reminder).show(childFragmentManager, null)
    }

    override fun onItemDeleteClick(reminder: Reminder) {
        viewModel.deleteReminder(reminder)
    }

    private fun initToolbar() {
        binding.remindersToolbar.setupAsTopLevelDestinationToolbar()
    }

    private fun initFab() {
        with(binding.remindersFab) {
            setOnClickListener {
                AddEditReminderDialogFragment.newInstance().show(childFragmentManager, null)
            }
        }
    }

    private fun initFragmentResultListeners() {
        childFragmentManager.setFragmentResultListener(
            AddEditReminderDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val reminder = bundle.getParcelable<Reminder>(AddEditReminderDialogFragment.REMINDER_DATA_KEY)!!
            val mode = bundle.getParcelable<ReminderFormViewModel.Mode>(AddEditReminderDialogFragment.MODE_DATA_KEY)!!

            viewModel.save(reminder, mode)
        }
    }

    private fun initObservers() {
        viewModel.action.observe(viewLifecycleOwner) {

            when (it.handleEvent()) {
                is RequestPermission -> {

                    firebaseAnalytics.logEvent(Events.ALARM_PERMISSION_REQUESTED, null)

                    context?.createAlertDialog(
                        title = getString(R.string.lbl_permission_required),
                        message = getString(R.string.reminder_permission_request_message),
                        positiveButtonText = getString(R.string.lbl_grant_permission),
                        onPositiveButtonClick = { _, _ ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                            }
                        }
                    )?.show()
                }
                else -> return@observe
            }
        }

        viewModel.reminders.observe(viewLifecycleOwner) {

            // With list adapter we need to invalid item decorations
            remindersAdapter.submitList(it) {
                with(binding.remindersList) {
                    post { invalidateItemDecorations() }
                }
            }
        }
    }

    private fun initRecyclerView() {
        remindersAdapter = RemindersAdapter().apply {
            setOnItemClickListener(this@RemindersFragment)
        }

        binding.remindersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = remindersAdapter
            addItemDecoration(VerticalSpaceItemDecoration(resources.getDimension(R.dimen.list_item_row_spacing)))
        }
    }
}