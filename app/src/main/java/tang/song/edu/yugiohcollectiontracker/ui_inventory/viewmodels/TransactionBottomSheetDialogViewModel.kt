package tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.InventoryRepository

class TransactionBottomSheetDialogViewModel @ViewModelInject constructor(
    private val inventoryRepository: InventoryRepository,
    private val cardRepository: CardRepository,
) : ViewModel() {


    fun insertTransaction(transactionData: TransactionData) {
        viewModelScope.launch() {
            inventoryRepository.insertTransaction(transactionData)
        }
    }

    suspend fun getCardDetailsById(cardId: Long) = cardRepository.getCardDetails(cardId)
}


