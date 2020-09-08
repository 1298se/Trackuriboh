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
    val inventoryId: Long,
    val transactionType: TransactionType?,
    val quantity: Int?,
    val date: Date,
    val seller: String?,
    val purchaseTrackingNumber: String?,
    val purchaseAmount: Double?,
    val buyer: String?,
    val soldAmount: Double?,
    val saleTrackingNumber: String?,
    val salePlatform: PlatformType?
)