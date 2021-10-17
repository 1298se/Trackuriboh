package sam.g.trackuriboh.ui_transaction_form.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.repository.CardRepository
import sam.g.trackuriboh.data.repository.InventoryRepository
import sam.g.trackuriboh.ui_transaction_form.models.TransactionDataModel
import javax.inject.Inject


sealed class TransactionResult {
    object SUCCESS: TransactionResult()
    object ERROR: TransactionResult()
}

@HiltViewModel
class TransactionBottomSheetDialogViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val cardRepository: CardRepository,
) : ViewModel() {
    val transactionData = TransactionDataModel()

    private val _transactionState: MutableStateFlow<TransactionResult?> = MutableStateFlow(null)
    val transactionState: StateFlow<TransactionResult?>
        get() = _transactionState

    fun insertTransaction() {
        viewModelScope.launch {
            try {
                inventoryRepository.insertTransaction(transactionData)
                _transactionState.value = TransactionResult.SUCCESS
            } catch (e: Exception) {
                _transactionState.value = TransactionResult.ERROR
            }
        }
    }
}


