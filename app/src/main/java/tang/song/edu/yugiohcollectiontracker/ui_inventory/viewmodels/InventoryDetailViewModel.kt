package tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import tang.song.edu.yugiohcollectiontracker.data.repository.InventoryRepository

class InventoryDetailViewModel @ViewModelInject constructor(
    private val inventoryRepository: InventoryRepository,
) : ViewModel() {
}