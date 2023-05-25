package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.MainGraphDirections
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.databinding.FragmentUserListDetailBinding
import sam.g.trackuriboh.ui.common.QuantitySelectorDialogFragment
import sam.g.trackuriboh.ui.common.SwipeToDeleteCallback
import sam.g.trackuriboh.ui.user_list.adapters.UserListEntryAdapter
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListDetailViewModel
import sam.g.trackuriboh.utils.addDividerItemDecoration
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.viewBinding
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class UserListDetailFragment : Fragment(), UserListEntryAdapter.OnInteractionListener {

    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics

    private val binding: FragmentUserListDetailBinding by viewBinding(FragmentUserListDetailBinding::inflate)

    private val viewModel: UserListDetailViewModel by viewModels()

    private lateinit var userListEntryAdapter: UserListEntryAdapter

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
        findNavController().safeNavigate(
            MainGraphDirections.actionGlobalCardDetailFragment(productId)
        )
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

        binding.userListDetailToolbar.title = viewModel.userList.name
    }

    private fun initFab() {
        with(binding.userListDetailAddCardFab) {
            setOnClickListener {
                findNavController().safeNavigate(
                    UserListDetailFragmentDirections.actionUserListDetailFragmentToAddToUserListFragment(
                        userList = viewModel.userList
                    )
                )
            }
        }
    }

    private fun initRecyclerView() {
        userListEntryAdapter = UserListEntryAdapter(this)

        binding.userListDetailList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListEntryAdapter
            addDividerItemDecoration()

            ItemTouchHelper(object : SwipeToDeleteCallback(context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.deleteEntry(viewHolder.itemId)
                }
            }).attachToRecyclerView(this)
        }
    }

    private fun initObservers() {
        viewModel.entries.observe(viewLifecycleOwner) {
            userListEntryAdapter.submitList(it)
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
