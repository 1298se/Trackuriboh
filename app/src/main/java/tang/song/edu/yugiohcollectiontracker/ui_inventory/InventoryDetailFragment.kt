package tang.song.edu.yugiohcollectiontracker.ui_inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentInventoryDetailBinding
import tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels.InventoryDetailViewModel
import tang.song.edu.yugiohcollectiontracker.viewBinding

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
    }
}