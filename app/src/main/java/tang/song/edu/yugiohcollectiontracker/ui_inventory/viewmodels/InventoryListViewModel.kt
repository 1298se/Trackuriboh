package tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory
import tang.song.edu.yugiohcollectiontracker.data.repository.InventoryRepository

class InventoryListViewModel @ViewModelInject constructor(
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