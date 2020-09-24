package tang.song.edu.yugiohcollectiontracker.ui_inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tang.song.edu.yugiohcollectiontracker.data.repository.CardInventoryRepository
import tang.song.edu.yugiohcollectiontracker.ui_card_detail.CardDetailViewModel
import javax.inject.Inject

class TransactionDialogViewModelFactory @Inject constructor(
    private val inventoryRepository: CardInventoryRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailViewModel::class.java)) {
            return TransactionDialogViewModel(
                inventoryRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}