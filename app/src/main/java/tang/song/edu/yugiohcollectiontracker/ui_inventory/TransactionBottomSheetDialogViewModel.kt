package tang.song.edu.yugiohcollectiontracker.ui_inventory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.BaseCardViewModel
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Transaction
import tang.song.edu.yugiohcollectiontracker.data.repository.CardInventoryRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class TransactionBottomSheetDialogViewModel @ViewModelInject constructor(
    private val inventoryRepository: CardInventoryRepository,
    cardRepository: CardRepository,
) : BaseCardViewModel(cardRepository) {
    fun insertTransaction(cardInventory: CardInventory, transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            inventoryRepository.insertTransaction(cardInventory, transaction)
        }

    }
}
