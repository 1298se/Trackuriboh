package sam.g.trackuriboh.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.relations.InventoryWithSkuMetadata
import sam.g.trackuriboh.data.repository.InventoryRepository
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.utils.UpdateTimer
import sam.g.trackuriboh.utils.refreshPricesIfNecessary
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val priceRepository: PriceRepository,
    private val updateTimer: UpdateTimer,
) : ViewModel() {

    sealed class ItemUiState {
        data class InventoryItemUiState(
            val data: InventoryWithSkuMetadata,
        ) : ItemUiState()

        data class SummaryUiState(
            val totalValue: Double,
            val totalUnrealizedProfit: Double,
            val totalRealizedProfit: Double,
            val totalPurchaseAmount: Double,
        ) : ItemUiState()
    }

    data class UiState(
        val itemUiModels: List<ItemUiState>
    )

    val inventory = inventoryRepository.getInventoryObservable().map { list ->
        refreshPricesIfNecessary(list.map { it.inventory.skuId }, updateTimer, priceRepository)

        var totalValue = 0.0
        var totalUnrealizedProfit = 0.0
        var totalRealizedProfit = 0.0
        var totalPurchaseAmount = 0.0

        val transformList: MutableList<ItemUiState> = list.map { inventory ->
            ItemUiState.InventoryItemUiState(inventory).also {
                totalValue += inventory.getTotalValue() ?: 0.0
                totalUnrealizedProfit += inventory.getTotalUnrealizedProfit() ?: 0.0
                totalRealizedProfit += inventory.inventory.totalRealizedProfit
                totalPurchaseAmount += inventory.getTotalCost()
            }
        }.filter { it.data.inventory.quantity > 0 }.toMutableList()

        transformList.add(
            0,
            ItemUiState.SummaryUiState(
                totalValue,
                totalUnrealizedProfit,
                totalRealizedProfit,
                totalPurchaseAmount
            )
        )
        UiState(
            transformList
        )
    }.asLiveData(viewModelScope.coroutineContext)

    fun deleteInventory(inventoryId: Long) {
        viewModelScope.launch {
            inventoryRepository.deleteInventory(inventoryId)
        }
    }

}