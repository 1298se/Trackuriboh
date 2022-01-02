package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.FragmentUserListsBinding
import sam.g.trackuriboh.ui.common.SimpleTextFieldDialogFragment
import sam.g.trackuriboh.ui.user_list.adapters.UserListsStateAdapter
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListsViewModel
import sam.g.trackuriboh.utils.setupAsTopLevelDestinationToolbar
import sam.g.trackuriboh.utils.viewBinding

/**
 * Fragment accessed from clicking "Watchlist" in bottom nav
 */
@AndroidEntryPoint
class UserListsFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private val binding: FragmentUserListsBinding by viewBinding(FragmentUserListsBinding::inflate)

    private val viewModel: UserListsViewModel by viewModels()

    private lateinit var userListsStateAdapter: UserListsStateAdapter

    // We use this to finish action mode when destination changes
    private val navigationDestinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        if (destination.parent?.id != R.id.userListsGraph) {
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

    override fun onStop() {
        super.onStop()

        findNavController().removeOnDestinationChangedListener(navigationDestinationChangedListener)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.action_rename_user_list -> {
                SimpleTextFieldDialogFragment.newInstance(
                    getString(R.string.rename_user_list_action_title)
                ).show(childFragmentManager, null)

                true
            }
            R.id.action_delete_user_list -> {
                viewModel.deleteCurrentList()
                true
            }
            else -> false
        }
    }

    private fun initToolbar() {
        binding.userListsToolbar.setupAsTopLevelDestinationToolbar()

        binding.userListsToolbar.inflateMenu(R.menu.user_list_toolbar)

        binding.userListsToolbar.setOnMenuItemClickListener(this)
    }

    private fun initFab() {
        with(binding.userListsFab) {
            setOnClickListener {
                CreateUserListBottomSheetFragment().show(childFragmentManager, null)
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
        with(childFragmentManager) {
            setFragmentResultListener(
                CreateUserListBottomSheetFragment.FRAGMENT_RESULT_REQUEST_KEY,
                viewLifecycleOwner
            ) { _, bundle ->
                val userList = bundle.getParcelable<UserList>(CreateUserListBottomSheetFragment.USER_LIST_DATA_KEY)!!
                viewModel.createUserList(userList)
            }


            setFragmentResultListener(
                SimpleTextFieldDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
                viewLifecycleOwner
            ) { _, bundle ->
                val name = bundle.getString(SimpleTextFieldDialogFragment.TEXT_DATA_KEY)

                if (name != null) {
                    viewModel.renameCurrentList(name)
                }
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
            if (it.isEmpty()) {
                binding.userListsViewPager.visibility = View.GONE
                binding.userListsTabLayout.visibility = View.GONE
            } else {
                binding.userListsViewPager.visibility = View.VISIBLE
                binding.userListsTabLayout.visibility = View.VISIBLE
            }

            if (binding.userListsViewPager.adapter == null) {
                initViewPager(it)
            } else {
                userListsStateAdapter.setUserLists(it)
            }
        }
    }

    private fun initNavigationObservers() {
        with(findNavController()) {
            addOnDestinationChangedListener(navigationDestinationChangedListener)
        }
    }

    private fun finishActionMode() {
        // Scope to main nav because if we use the current back stack entry, it will be removed when onDestinationChanged
        // gets called
        findNavController().getBackStackEntry(R.id.mainGraph).savedStateHandle.set(ACTION_FINISH_ACTION_MODE, true)
    }
}