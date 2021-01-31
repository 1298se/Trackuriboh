package tang.song.edu.yugiohcollectiontracker.ui_transaction_form.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.InventoryRepository
import tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels.TransactionData
import tang.song.edu.yugiohcollectiontracker.ui_transaction_form.models.TransactionFormModel

class TransactionBottomSheetDialogViewModel @ViewModelInject constructor(
    private val inventoryRepository: InventoryRepository,
    private val cardRepository: CardRepository,
) : ViewModel() {
    val formData = TransactionFormModel()

    fun insertTransaction(transactionData: TransactionData? = null) {
/*        viewModelScope.launch() {
            inventoryRepository.insertTransaction(transactionData)
        }*/
        Log.d("hello", "hi")
    }

    suspend fun getCardDetailsById(cardId: Long) = cardRepository.getCardDetails(cardId).also { formData.cardName = it.card.name }
}


