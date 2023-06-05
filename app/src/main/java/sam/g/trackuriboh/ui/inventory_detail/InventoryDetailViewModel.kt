package sam.g.trackuriboh.ui.inventory_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.entities.InventoryTransaction
import sam.g.trackuriboh.data.repository.InventoryRepository
import sam.g.trackuriboh.data.repository.InventoryTransactionRepository
import javax.inject.Inject

@HiltViewModel
class InventoryDetailViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val inventoryTransactionRepository: InventoryTransactionRepository,
    private val state: SavedStateHandle,
) : ViewModel() {
    val inventoryId = state.get<Long>("inventoryId")!!

    /**
     * Flows are not lifecycle aware! If we don't pass the [viewModelScope] CoroutineContext, it will not
     * cancel until a timeout. This is a problem if the inventory item gets deleted, then
     * See [https://developer.android.com/reference/kotlin/androidx/lifecycle/package-summary#(kotlinx.coroutines.flow.Flow).asLiveData(kotlin.coroutines.CoroutineContext,java.time.Duration]
     * and [https://stackoverflow.com/questions/63806895/is-flow-lifecycle-aware-as-livedata]
     */
    val inventoryWithSkuMetadataAndTransactions =
        inventoryRepository.getInventoryWithSkuMetadataAndTransactionsObservable(inventoryId).map {
            it?.copy(transactions = it.getSortedTransactionsByDateDesc())
        }.asLiveData()

    fun deleteTransaction(transaction: InventoryTransaction) {
        viewModelScope.launch {
            inventoryTransactionRepository.deleteTransaction(transaction)
        }
    }
}