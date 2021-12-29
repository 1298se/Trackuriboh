package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.FragmentUserListsBinding
import sam.g.trackuriboh.ui.user_list.adapters.UserListsStateAdapter
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListsViewModel
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.setupAsTopLevelDestinationToolbar
import sam.g.trackuriboh.utils.viewBinding

/**
 * Fragment accessed from clicking "Watchlist" in bottom nav
 */
@AndroidEntryPoint
class UserListsFragment : Fragment() {
    private val binding: FragmentUserListsBinding by viewBinding(FragmentUserListsBinding::inflate)

    private val viewModel: UserListsViewModel by hiltNavGraphViewModels(R.id.user_lists_nav)

    private lateinit var userListsStateAdapter: UserListsStateAdapter

    private val navigationDestinationChangedListener = NavController.OnDestinationChangedListener { navController, destination, _ ->
        if (destination.parent?.id != R.id.user_lists_nav) {
            // When we change tabs we want to remove actionmode
            finishActionMode()
        }
    }

    companion object {
        const val ACTION_FINISH_ACTION_MODE = "UserListsFragment_actionFinishActionMode"
    }

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
        initViewModelObservers()
        initFragmentResultListeners()
        initNavigationObservers()
    }

    private fun initToolbar() {
        binding.userListsToolbar.setupAsTopLevelDestinationToolbar()
    }

    private fun initFab() {
        with(binding.userListsFab) {
            setOnClickListener {
                findNavController().safeNavigate(
                    UserListsFragmentDirections.actionUserListsFragmentToCreateUserListBottomSheetDialogFragment()
                )
            }
        }
    }

    private fun initViewPager(lists: List<UserList>) {
        with(binding.userListsViewPager) {
            adapter = UserListsStateAdapter(this@UserListsFragment).also {
                it.setUserLists(lists)
                userListsStateAdapter = it
            }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.setCurrentTabPosition(position)
                }
            })
        }

        TabLayoutMediator(binding.userListsTabLayout, binding.userListsViewPager) { tab, position ->
            tab.text = viewModel.userLists.value?.get(position)?.name
        }.attach()
    }

    private fun initFragmentResultListeners() {

        with(parentFragmentManager) {
            setFragmentResultListener(
                CreateUserListBottomSheetDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
                viewLifecycleOwner
            ) { _, bundle ->
                val userList = bundle.getParcelable<UserList>(CreateUserListBottomSheetDialogFragment.USER_LIST_DATA_KEY)!!
                viewModel.createUserList(userList)
            }
        }
    }

    private fun initViewModelObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            finishActionMode()

            with(binding.userListsViewPager) {
                if (currentItem != it.currentSelectedTabPosition) {
                    setCurrentItem(it.currentSelectedTabPosition, true)
                }
            }
        }

        viewModel.userLists.observe(viewLifecycleOwner) {
            if (binding.userListsViewPager.adapter == null) {
                initViewPager(it)
            } else {
                userListsStateAdapter.setUserLists(it)
            }
        }
    }

    private fun initNavigationObservers() {
        findNavController().addOnDestinationChangedListener(navigationDestinationChangedListener)
    }

    private fun finishActionMode() {
        // Scope to main nav because if we use the current back stack entry, it will be removed when onDestinationChanged
        // gets called
        findNavController().getBackStackEntry(R.id.main_nav).savedStateHandle.set(ACTION_FINISH_ACTION_MODE, true)
    }

    override fun onStop() {
        super.onStop()

        findNavController().removeOnDestinationChangedListener(navigationDestinationChangedListener)
    }
}