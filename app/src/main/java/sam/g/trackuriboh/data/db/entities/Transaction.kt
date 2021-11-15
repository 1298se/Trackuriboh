package sam.g.trackuriboh.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import sam.g.trackuriboh.data.types.PlatformType
import sam.g.trackuriboh.data.types.TransactionType

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