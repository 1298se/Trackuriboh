package tang.song.edu.yugiohcollectiontracker.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class CardInventory(
    @PrimaryKey(autoGenerate = true)
    val inventoryId: Long = 0,
    val cardId: Long,
    val cardName: String?,
    val cardNumber: String,
    val rarity: String?,
    val quantity: Int?,
    val avgPurchasePrice: Double?,
    val avgSalePrice: Double?
) : Parcelable