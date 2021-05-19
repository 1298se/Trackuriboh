package tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tang.song.edu.yugiohcollectiontracker.data.repository.InventoryRepository
import javax.inject.Inject

@HiltViewModel
class InventoryDetailViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
) : ViewModel() {
}