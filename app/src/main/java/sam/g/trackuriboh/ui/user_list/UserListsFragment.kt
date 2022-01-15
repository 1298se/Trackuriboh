package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.FragmentUserListsBinding
import sam.g.trackuriboh.ui.common.SimpleTextFieldDialogFragment
import sam.g.trackuriboh.ui.common.VerticalSpaceItemDecoration
import sam.g.trackuriboh.ui.user_list.adapters.UserListAdapter
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListsViewModel
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.setupAsTopLevelDestinationToolbar
import sam.g.trackuriboh.utils.viewBinding

/**
 * Fragment accessed from clicking "Watchlist" in bottom nav
 */
@AndroidEntryPoint
class UserListsFragment : Fragment(), Toolbar.OnMenuItemClickListener, UserListAdapter.OnItemClickListener {
    private val binding: FragmentUserListsBinding by viewBinding(FragmentUserListsBinding::inflate)

    private val viewModel: UserListsViewModel by viewModels()

    private lateinit var userListAdapter: UserListAdapter

    companion object {
        const val EXTRA_RENAME_USER_LIST = "UserListsFragment_extraRenameUserList"
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
        initRecyclerView()
        initObservers()
        initFragmentResultListeners()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.action_create_user_list -> {
                CreateUserListBottomSheetFragment().show(childFragmentManager, null)

                true
            }
            else -> false
        }
    }

    override fun onItemClick(userList: UserList) {
        findNavController().safeNavigate(
            UserListsFragmentDirections.actionUserListsFragmentToUserListDetailFragment(userList)
        )
    }

    override fun onRenameClick(userList: UserList) {
        SimpleTextFieldDialogFragment.newInstance(
            getString(R.string.rename_user_list_action_title),
            bundleOf(EXTRA_RENAME_USER_LIST to userList)
        ).show(childFragmentManager, null)

        true
    }

    override fun onDeleteClick(userList: UserList) {
        viewModel.deleteUserList(userList)
    }

    private fun initToolbar() {
        with(binding.userListsToolbar) {
            setupAsTopLevelDestinationToolbar()

            inflateMenu(R.menu.user_list_toolbar)

            setOnMenuItemClickListener(this@UserListsFragment)
        }

    }

    private fun initRecyclerView() {
        userListAdapter = UserListAdapter().apply {
            setOnItemClickListener(this@UserListsFragment)
        }

        binding.userListsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
            addItemDecoration(VerticalSpaceItemDecoration(resources.getDimension(R.dimen.list_item_large_row_spacing)))
        }
    }

    private fun initObservers() {
        viewModel.userLists.observe(viewLifecycleOwner) {
            userListAdapter.submitList(it) {
                with(binding.userListsList) {
                    post { invalidateItemDecorations() }
                }
            }
        }
    }

    private fun initFragmentResultListeners() {
        with(childFragmentManager) {
            // Observes renaming list
            setFragmentResultListener(
                SimpleTextFieldDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
                viewLifecycleOwner
            ) { _, bundle ->
                val name = bundle.getString(SimpleTextFieldDialogFragment.TEXT_DATA_KEY)
                val userList = bundle.getBundle(SimpleTextFieldDialogFragment.EXTRAS_DATA_KEY)?.getParcelable<UserList>(
                    EXTRA_RENAME_USER_LIST
                )

                if (userList != null && name != null) {
                    viewModel.renameCurrentList(userList, name)
                }
            }
        }
    }
}