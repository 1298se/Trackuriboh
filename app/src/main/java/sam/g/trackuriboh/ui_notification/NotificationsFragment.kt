package sam.g.trackuriboh.ui_notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import sam.g.trackuriboh.databinding.FragmentNotificationsBinding
import sam.g.trackuriboh.utils.handleNavigationAction
import sam.g.trackuriboh.utils.setupAsTopLevelDestinationToolbar
import sam.g.trackuriboh.utils.viewBinding

class NotificationsFragment : Fragment() {
    private val binding: FragmentNotificationsBinding by viewBinding(FragmentNotificationsBinding::inflate)

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    private val viewModel: NotificationsViewModel by viewModels()

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
    }

    private fun initToolbar() {
        binding.remindersToolbar.setupAsTopLevelDestinationToolbar()
    }

    private fun initFab() {
        with(binding.remindersFab) {
            setOnClickListener {
                handleNavigationAction(NotificationsFragmentDirections.actionNotificationsFragmentToNotificationFormFragment())
            }

        }
    }
}