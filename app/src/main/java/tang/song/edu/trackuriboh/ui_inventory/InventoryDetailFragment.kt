package tang.song.edu.trackuriboh.ui_inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import tang.song.edu.trackuriboh.BaseFragment
import tang.song.edu.trackuriboh.databinding.FragmentInventoryDetailBinding
import tang.song.edu.trackuriboh.ui_inventory.viewmodels.InventoryDetailViewModel
import tang.song.edu.trackuriboh.viewBinding

class InventoryDetailFragment : BaseFragment() {
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