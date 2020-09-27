package tang.song.edu.yugiohcollectiontracker.ui_inventory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import tang.song.edu.yugiohcollectiontracker.BaseCardViewModel
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo
import tang.song.edu.yugiohcollectiontracker.data.models.PlatformType
import tang.song.edu.yugiohcollectiontracker.data.models.TransactionType
import tang.song.edu.yugiohcollectiontracker.data.repository.CardInventoryRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import java.util.*

class TransactionBottomSheetDialogViewModel(private val inventoryRepository: CardInventoryRepository, private val cardRepository: CardRepository) : BaseCardViewModel(cardRepository) {
    fun insertTransaction(card: Card, cardNumber: String, rarity: String, transactionType: TransactionType, quantity: Int, date: Date, buyerSellerName: String, trackingNumber: String, amount: Double, salePlatform: PlatformType) {

    }
}
