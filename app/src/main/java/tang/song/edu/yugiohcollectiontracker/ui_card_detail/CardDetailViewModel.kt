package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory
import tang.song.edu.yugiohcollectiontracker.data.repository.CardInventoryRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardDetailViewModel(private val cardRepository: CardRepository, private val cardInventoryRepository: CardInventoryRepository) : ViewModel() {
    fun getCardDetailsById(cardId: Long) = liveData {
        emit(cardRepository.getCardDetails(cardId))
    }

    fun insertInventory(inventory: CardInventory) {
        viewModelScope.launch(Dispatchers.IO) {
            val inventoryId = cardInventoryRepository.insertInventory(inventory)

            val inventoryWithTransactions = cardInventoryRepository.getInventoryWithTransactions(inventoryId)

            Log.d("ADSFAS", "asdfadasfsa")
        }
    }
}
