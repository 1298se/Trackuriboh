package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.FragmentUserListEntryDetailBinding
import sam.g.trackuriboh.ui.transaction.AddTransactionDialogFragment
import sam.g.trackuriboh.ui.user_list.adapters.UserTransactionAdapter
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class UserListEntryDetailFragment : Fragment(), UserTransactionAdapter.OnInteractionListener {
    private val binding: FragmentUserListEntryDetailBinding by viewBinding(FragmentUserListEntryDetailBinding::inflate)

    private val viewModel: UserListEntryDetailViewModel by viewModels()

    private lateinit var userTransactionAdapter: UserTransactionAdapter

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

        viewModel.transactions.observe(viewLifecycleOwner) {
            userTransactionAdapter.submitList(it)
        }
    }

    private fun initToolbar() {
        binding.userListEntryDetailToolbar.setupWithNavController(findNavController())
    }

    private fun initRecyclerView() {
        userTransactionAdapter = UserTransactionAdapter(this)

        binding.userTransactionsList.apply {
            adapter = userTransactionAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onAddNewTransactionClick() {
        AddTransactionDialogFragment.newInstance(viewModel.listId, viewModel.skuId).show(childFragmentManager, null)
    }
}