package sam.g.trackuriboh.ui.collection

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
import sam.g.trackuriboh.databinding.FragmentCollectionDetailBinding
import sam.g.trackuriboh.ui.collection.CollectionsFragment.Companion.ACTION_FINISH_ACTION_MODE
import sam.g.trackuriboh.ui.collection.adapters.CollectionEntryAdapter
import sam.g.trackuriboh.ui.collection.viewmodels.CollectionDetailViewModel
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class CollectionDetailFragment : Fragment(), CollectionEntryAdapter.OnItemClickListener {

    companion object {
        // This is the same value as the navArg name so that the SavedStateHandle can acess from either
        const val ARG_WATCHLIST_ID = "watchlistId"

        fun newInstance(watchlistId: Long) =
            CollectionDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_WATCHLIST_ID, watchlistId)
                }
            }
    }

    private val binding: FragmentCollectionDetailBinding by viewBinding(FragmentCollectionDetailBinding::inflate)

    private val viewModel: CollectionDetailViewModel by viewModels()

    private lateinit var collectionEntryAdapter: CollectionEntryAdapter

    private var actionMode: ActionMode? = null

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater?.inflate(R.menu.collection_contextual_action, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_remove_collection_entries -> {
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
        binding.collectionDetailList.apply {
            layoutManager = LinearLayoutManager(context)

            adapter = CollectionEntryAdapter().apply {
                setOnItemClickListener(this@CollectionDetailFragment)
                collectionEntryAdapter = this
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
            collectionEntryAdapter.submitList(it.entries)

             if (it.actionModeActive) {
                 if (actionMode == null) {
                     actionMode = activity?.startActionMode(actionModeCallback)
                     collectionEntryAdapter.setInActionMode(true)
                 }
            } else {
                collectionEntryAdapter.setInActionMode(false)
                actionMode?.finish()
                actionMode = null
            }

            actionMode?.title = it.actionModeTitle
        }
    }

    override fun onCollectionEntryClick(entry: UserListEntry) {
        findNavController().safeNavigate(
            MainNavDirections.actionGlobalCardDetailActivity(entry.skuId)
        )
    }

    override fun onAddCardClick() {
        TODO("Not yet implemented")
    }

    override fun onCollectionEntryLongClick(entry: UserListEntry) {
        viewModel.setActionMode(true)
        viewModel.setCollectionEntryChecked(entry, true)
    }

    override fun onCollectionEntryChecked(entry: UserListEntry, isChecked: Boolean) {
        viewModel.setCollectionEntryChecked(entry, isChecked)
    }
}