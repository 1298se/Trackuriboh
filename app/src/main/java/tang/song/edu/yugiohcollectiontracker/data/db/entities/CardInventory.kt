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
    val cardName: String,
    val cardNumber: String,
    var cardImageURL: String?,
    val rarity: String,
    var quantity: Int = 0,
    var soldQuantity: Int = 0,
    var currentAvgPurchasePrice: Double = 0.toDouble(),
    var avgSalePrice: Double = 0.toDouble()
) : Parcelable