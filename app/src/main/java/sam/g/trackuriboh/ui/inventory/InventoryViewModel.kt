package sam.g.trackuriboh.ui.inventory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.relations.InventoryWithSkuMetadataAndTransactions
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
            val data: InventoryWithSkuMetadataAndTransactions,
        ) : ItemUiState()

        data class SummaryUiState(
            val totalValue: Double,
            val totalUnrealizedProfit: Double,
            val totalRealizedProfit: Double,
            val totalPurchaseAmount: Double,
            val showOutOfStock: Boolean,
        ) : ItemUiState()
    }

    data class UiState(
        val itemUiModels: List<ItemUiState>
    )

    private val showOutOfStock = MutableLiveData(false)

    val inventory = showOutOfStock.switchMap { showOutOfStock ->
        inventoryRepository.getInventoryObservable().map { list ->
            refreshPricesIfNecessary(
                list.map { it.inventoryWithSkuMetadata.inventory.skuId },
                updateTimer,
                priceRepository
            )

            var totalValue = 0.0
            var totalUnrealizedProfit = 0.0
            var totalRealizedProfit = 0.0
            var totalPurchaseAmount = 0.0

            val transformList: MutableList<ItemUiState> = list.map { inventory ->
                ItemUiState.InventoryItemUiState(inventory).also {
                    totalValue += it.data.totalValue
                    totalUnrealizedProfit += it.data.totalUnrealizedProfit ?: 0.0
                    totalRealizedProfit += it.data.totalRealizedProfit
                    totalPurchaseAmount += it.data.totalPurchaseAmount
                }
            }.filter { if (!showOutOfStock) it.data.quantity > 0 else true }.toMutableList()

            transformList.add(
                0,
                ItemUiState.SummaryUiState(
                    totalValue,
                    totalUnrealizedProfit,
                    totalRealizedProfit,
                    totalPurchaseAmount,
                    showOutOfStock
                )
            )
            UiState(
                transformList
            )
        }.asLiveData(viewModelScope.coroutineContext)
    }

    fun deleteInventory(inventoryId: Long) {
        viewModelScope.launch {
            inventoryRepository.deleteInventory(inventoryId)
        }
    }

    fun showOutOfStock(enabled: Boolean) {
        showOutOfStock.value = enabled
    }

}