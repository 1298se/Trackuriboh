package tang.song.edu.trackuriboh.ui_inventory.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tang.song.edu.trackuriboh.data.repository.InventoryRepository
import javax.inject.Inject

@HiltViewModel
class InventoryDetailViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
) : ViewModel() {
}