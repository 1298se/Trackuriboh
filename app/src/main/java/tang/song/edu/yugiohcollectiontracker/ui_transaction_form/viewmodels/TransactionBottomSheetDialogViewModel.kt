package tang.song.edu.yugiohcollectiontracker.ui_transaction_form.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.InventoryRepository
import tang.song.edu.yugiohcollectiontracker.ui_transaction_form.models.TransactionDataModel


sealed class TransactionResult {
    object SUCCESS: TransactionResult()
    object ERROR: TransactionResult()
}

class TransactionBottomSheetDialogViewModel @ViewModelInject constructor(
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

    suspend fun getCardDetailsById(cardId: Long) = cardRepository.getCardDetails(cardId).also {
        transactionData.cardId = it.card.cardId
        transactionData.cardName = it.card.name
        transactionData.cardImageURL = it.card.getDefaultImageURL()
    }
}


