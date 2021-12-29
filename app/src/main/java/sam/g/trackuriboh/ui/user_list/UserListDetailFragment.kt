package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.MainNavDirections
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.databinding.FragmentUserListDetailBinding
import sam.g.trackuriboh.ui.user_list.UserListsFragment.Companion.ACTION_FINISH_ACTION_MODE
import sam.g.trackuriboh.ui.user_list.adapters.UserListEntryAdapter
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListDetailViewModel
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class UserListDetailFragment : Fragment(), UserListEntryAdapter.OnItemClickListener {

    companion object {
        // This is the same value as the navArg name so that the SavedStateHandle can acess from either
        const val ARG_WATCHLIST_ID = "watchlistId"

        fun newInstance(watchlistId: Long) =
            UserListDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_WATCHLIST_ID, watchlistId)
                }
            }
    }

    private val binding: FragmentUserListDetailBinding by viewBinding(FragmentUserListDetailBinding::inflate)

    private val viewModel: UserListDetailViewModel by viewModels()

    private lateinit var userListEntryAdapter: UserListEntryAdapter

    private var actionMode: ActionMode? = null

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater?.inflate(R.menu.user_list_detail_contextual_action, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_remove_user_list_entries -> {
                    viewModel.deleteSelectedItems()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            viewModel.setActionMode(false)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initNavigationObservers()
        initObservers()
    }

    private fun initRecyclerView() {
        binding.userListDetailList.apply {
            layoutManager = LinearLayoutManager(context)

            adapter = UserListEntryAdapter().apply {
                setOnItemClickListener(this@UserListDetailFragment)
                userListEntryAdapter = this
            }

            addItemDecoration(MaterialDividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
        }
    }

    private fun initNavigationObservers() {

        // We use the navigation back stack entry to get the callback to finish the action mode. We do this
        // Instead of using Fragment Result API since Fragment Result API can only have one listener at a time
        val savedStateHandle = findNavController().getBackStackEntry(R.id.main_nav).savedStateHandle

        savedStateHandle.getLiveData<Boolean>(ACTION_FINISH_ACTION_MODE).observe(
            viewLifecycleOwner
        ) {
            actionMode?.finish()
        }
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            userListEntryAdapter.submitList(it.entries)

             if (it.actionModeActive) {
                 if (actionMode == null) {
                     actionMode = activity?.startActionMode(actionModeCallback)
                     userListEntryAdapter.setInActionMode(true)
                 }
            } else {
                userListEntryAdapter.setInActionMode(false)
                actionMode?.finish()
                actionMode = null
            }

            actionMode?.title = it.actionModeTitle
        }
    }

    override fun onListEntryClick(entry: UserListEntry) {
        findNavController().safeNavigate(
            MainNavDirections.actionGlobalCardDetailActivity(entry.skuId)
        )
    }

    override fun onAddCardClick() {
        TODO("Not yet implemented")
    }

    override fun onListEntryLongClick(entry: UserListEntry) {
        viewModel.setActionMode(true)
        viewModel.setUserListEntryChecked(entry, true)
    }

    override fun onListEntryChecked(entry: UserListEntry, isChecked: Boolean) {
        viewModel.setUserListEntryChecked(entry, isChecked)
    }
}