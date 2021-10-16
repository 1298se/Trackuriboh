package tang.song.edu.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import tang.song.edu.trackuriboh.data.types.EditionType

@Parcelize
@Entity
data class CardInventory(
    @PrimaryKey(autoGenerate = true)
    val inventoryId: Long = 0,
    var lastTransaction: Long,
    val cardId: Long,
    val cardName: String,
    val cardNumber: String,
    var cardImageURL: String?,
    val rarity: String?,
    val edition: EditionType?,
    var quantity: Int = 0,
    var soldQuantity: Int = 0,
    var curAvgPurchasePrice: Double = 0.toDouble(),
    var profitAndLoss: Double = 0.toDouble()
) : Parcelable
