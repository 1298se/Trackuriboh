package sam.g.trackuriboh.ui_inventory.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.repository.InventoryRepository
import javax.inject.Inject

@HiltViewModel
class InventoryDetailViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
) : ViewModel() {
}