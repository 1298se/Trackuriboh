package tang.song.edu.yugiohcollectiontracker.ui_inventory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.BaseCardViewModel
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Transaction
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo
import tang.song.edu.yugiohcollectiontracker.data.models.PlatformType
import tang.song.edu.yugiohcollectiontracker.data.models.TransactionType
import tang.song.edu.yugiohcollectiontracker.data.repository.CardInventoryRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import java.util.*

class TransactionBottomSheetDialogViewModel(private val inventoryRepository: CardInventoryRepository, cardRepository: CardRepository) : BaseCardViewModel(cardRepository) {
    fun insertTransaction(cardInventory: CardInventory, transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            inventoryRepository.insertTransaction(cardInventory, transaction)
        }

    }
}
