package sam.g.trackuriboh.ui_inventory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.CardInventory
import sam.g.trackuriboh.data.repository.InventoryRepository
import javax.inject.Inject

@HiltViewModel
class InventoryListViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
) : ViewModel() {
    private var currentInventoryList: Flow<PagingData<CardInventory>>? = null

    fun getInventoryList(): Flow<PagingData<CardInventory>> {
        val lastResult = currentInventoryList
        if (lastResult != null) {
            return lastResult
        }

        val newResult = inventoryRepository.getInventoryList()
        currentInventoryList = newResult
        return newResult
    }
}