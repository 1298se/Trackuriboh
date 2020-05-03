package tang.song.edu.yugiohcollectiontracker.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import tang.song.edu.yugiohcollectiontracker.data.models.CardType

@Parcelize
@Entity
data class Card(
    @PrimaryKey(autoGenerate = false)
    val cardId: Long,
    val name: String,
    val type: CardType,
    val desc: String?,
    val atk: Int?,
    val def: Int?,
    val level: Int?,
    val race: String?,
    val attribute: String?,
    val archetype: String?,
    val scale: Int?,
    val cardImageList: List<String>?
) : Parcelable
