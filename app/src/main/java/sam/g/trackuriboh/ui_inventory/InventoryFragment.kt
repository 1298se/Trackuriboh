package sam.g.trackuriboh.ui_inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentInventoryBinding
import sam.g.trackuriboh.ui_inventory.adapters.InventoryListAdapter
import sam.g.trackuriboh.ui_inventory.viewmodels.InventoryListViewModel
import sam.g.trackuriboh.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class InventoryFragment : Fragment(), InventoryListAdapter.OnItemClickListener {
    @Inject
    lateinit var mAdapter: InventoryListAdapter

    private val binding by viewBinding(FragmentInventoryBinding::inflate)

    private val mViewModel: InventoryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this) { activity?.finish() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initRecyclerView()

        lifecycleScope.launch {
            mViewModel.getInventoryList().collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    override fun onItemClick(inventoryId: Long) {
        val action = InventoryFragmentDirections.actionInventoryFragmentToInventoryDetailFragment(inventoryId)

        findNavController().navigate(action)
    }

    private fun initToolbar() {
        val navController = findNavController()
        val navGraphIds = setOf(R.id.inventoryFragment, R.id.transactionFragment, R.id.databaseFragment)

        val appBarConfiguration = AppBarConfiguration(topLevelDestinationIds = navGraphIds)

        binding.inventoryToolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun initRecyclerView() {
        mAdapter.setOnItemClickListener(this)
        binding.inventoryList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
    }
}
