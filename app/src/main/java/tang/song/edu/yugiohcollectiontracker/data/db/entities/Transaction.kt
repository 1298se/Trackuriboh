package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import tang.song.edu.yugiohcollectiontracker.data.models.PlatformType
import tang.song.edu.yugiohcollectiontracker.data.models.TransactionType
import java.util.*

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Long = 0,
    @ForeignKey(entity = CardInventory::class, parentColumns = ["inventoryId"], childColumns = ["inventoryId"], onDelete = ForeignKey.CASCADE)
    var inventoryId: Long,
    val transactionType: TransactionType?,
    val quantity: Int?,
    val date: Date,
    val buyerSellerName: String?,
    val trackingNumber: String?,
    val amount: Double?,
    val salePlatform: PlatformType?
)