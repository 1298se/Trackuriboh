package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    indices = [Index(value = ["name"])]
)
data class CardRarity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
) : Parcelable
