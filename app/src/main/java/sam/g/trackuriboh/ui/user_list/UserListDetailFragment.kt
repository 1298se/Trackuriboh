package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.databinding.FragmentUserListDetailBinding
import sam.g.trackuriboh.ui.common.QuantitySelectorDialogFragment
import sam.g.trackuriboh.ui.user_list.adapters.UserListEntryAdapter
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListDetailViewModel
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class UserListDetailFragment : Fragment(), UserListEntryAdapter.OnItemClickListener {

    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics

    private val args: UserListDetailFragmentArgs by navArgs()

    private val binding: FragmentUserListDetailBinding by viewBinding(FragmentUserListDetailBinding::inflate)

    private val viewModel: UserListDetailViewModel by viewModels()

    private lateinit var userListEntryAdapter: UserListEntryAdapter

    private var actionMode: ActionMode? = null

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            firebaseAnalytics.logEvent(Events.ACTION_MODE_ON, null)

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
            firebaseAnalytics.logEvent(Events.ACTION_MODE_OFF, null)

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

        initToolbar()
        initFab()
        initRecyclerView()
        initObservers()
        initFragmentResultListeners()
    }

    override fun onListEntryClick(productId: Long) {
        /*findNavController().safeNavigate(
            MainGraphDirections.actionGlobalCardDetailActivity(productId)
        )*/
    }

    override fun onDestroyView() {
        actionMode?.finish()

        super.onDestroyView()
    }

    override fun onListEntryLongClick(skuId: Long) {
        viewModel.setActionMode(true)
        viewModel.setUserListEntryChecked(skuId, true)
    }

    override fun onListEntryChecked(skuId: Long, isChecked: Boolean) {
        viewModel.setUserListEntryChecked(skuId, isChecked)
    }

    override fun onQuantityTextClick(entry: UserListEntry) {
        viewModel.setCurrentEditEntry(entry)

        QuantitySelectorDialogFragment.newInstance(entry.quantity).show(childFragmentManager, null)
    }

    private fun initToolbar() {
        binding.userListDetailToolbar.setupWithNavController(
            findNavController(),
            AppBarConfiguration.Builder().setFallbackOnNavigateUpListener {
                activity?.finish()
                true
            }.build()
        )

        binding.userListDetailToolbar.title = args.userList.name
    }

    private fun initFab() {
        with(binding.userListDetailAddCardFab) {
            setOnClickListener {
                findNavController().safeNavigate(
                    UserListDetailFragmentDirections.actionUserListDetailFragmentToCardSelectionFragment(
                        userList = args.userList
                    )
                )
            }
        }
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

    private fun initFragmentResultListeners() {
       childFragmentManager.setFragmentResultListener(
            QuantitySelectorDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val quantity = bundle.getInt(QuantitySelectorDialogFragment.QUANTITY_DATA_KEY)

            viewModel.updateCurrentEditEntryQuantity(quantity)
        }
    }
}
