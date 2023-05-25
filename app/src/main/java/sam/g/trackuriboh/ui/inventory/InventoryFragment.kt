package sam.g.trackuriboh.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.FragmentInventoryBinding
import sam.g.trackuriboh.ui.common.SwipeToDeleteCallback
import sam.g.trackuriboh.utils.addDividerItemDecoration
import sam.g.trackuriboh.utils.setupAsTopLevelDestinationToolbar
import sam.g.trackuriboh.utils.viewBinding


@AndroidEntryPoint
class InventoryFragment : Fragment(),
    InventoryAdapter.OnInteractionListener {
    private val binding: FragmentInventoryBinding by viewBinding(FragmentInventoryBinding::inflate)

    private val viewModel: InventoryViewModel by viewModels()

    private lateinit var inventoryAdapter: InventoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFab()
        initToolbar()
        initRecyclerView()

        viewModel.inventory.observe(viewLifecycleOwner) {
            inventoryAdapter.submitList(it.itemUiModels)
        }
    }

    override fun onItemClick(inventoryId: Long) {
        findNavController().navigate(
            InventoryFragmentDirections.actionInventoryFragmentToInventoryDetailFragment(
                inventoryId
            )
        )
    }

    private fun initToolbar() {
        with(binding.inventoryToolbar) {
            setupAsTopLevelDestinationToolbar()
        }
    }

    private fun initRecyclerView() {
        inventoryAdapter = InventoryAdapter(this)

        binding.inventoryList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = inventoryAdapter
            addDividerItemDecoration()

            ItemTouchHelper(object : SwipeToDeleteCallback(context) {

                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    return if (viewHolder is InventoryAdapter.InventoryViewHolder) {
                        super.getMovementFlags(recyclerView, viewHolder)
                    } else {
                        0
                    }
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.deleteInventory(viewHolder.itemId)
                }
            }).attachToRecyclerView(this)
        }
    }

    private fun initFab() {
        binding.inventoryFab.setOnClickListener {
            findNavController().navigate(InventoryFragmentDirections.actionInventoryFragmentToAddTransactionFragment())
        }
    }
}
