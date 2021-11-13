package sam.g.trackuriboh.ui_inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import sam.g.trackuriboh.databinding.FragmentInventoryDetailBinding
import sam.g.trackuriboh.ui_inventory.viewmodels.InventoryDetailViewModel
import sam.g.trackuriboh.viewBinding

class InventoryDetailFragment : Fragment() {
    private val binding by viewBinding(FragmentInventoryDetailBinding::inflate)

    private val viewModel: InventoryDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
    }

    private fun initToolbar() {
        binding.inventoryDetailToolbar.setupWithNavController(findNavController())
    }
}