package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import tang.song.edu.yugiohcollectiontracker.data.types.PlatformType
import tang.song.edu.yugiohcollectiontracker.data.types.TransactionType

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Long = 0,
    var inventoryId: Long,
    val transactionType: TransactionType?,
    val quantity: Int?,
    val date: Long?,
    val partyName: String?,
    val trackingNumber: String?,
    val price: Double?,
    val platformType: PlatformType?
)