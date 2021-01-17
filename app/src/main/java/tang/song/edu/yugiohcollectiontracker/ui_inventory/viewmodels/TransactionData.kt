package tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels

import tang.song.edu.yugiohcollectiontracker.data.types.EditionType
import tang.song.edu.yugiohcollectiontracker.data.types.PlatformType
import tang.song.edu.yugiohcollectiontracker.data.types.TransactionType
import java.util.*

data class TransactionData(
    val cardId: Long,
    val cardName: String,
    val cardNumber: String,
    val cardImageURL: String?,
    val rarity: String?,
    val edition: EditionType?,
    val quantity: Int,
    val transactionType: TransactionType,
    val date: Date,
    val buyerSellerName: String?,
    val trackingNumber: String?,
    val salePlatform: PlatformType,
    val price: Double?,
)
