package tang.song.edu.yugiohcollectiontracker.ui_inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentInventoryListBinding
import tang.song.edu.yugiohcollectiontracker.ui_inventory.adapters.InventoryListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels.InventoryListViewModel
import tang.song.edu.yugiohcollectiontracker.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class InventoryListFragment : BaseFragment() {
    @Inject
    lateinit var mAdapter: InventoryListAdapter

    private val binding by viewBinding(FragmentInventoryListBinding::inflate)

    private val mViewModel: InventoryListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        lifecycleScope.launch {
            mViewModel.getInventoryList().collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerView() {
        // mAdapter.setOnItemClickListener(this)
        binding.inventoryList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
    }
}
