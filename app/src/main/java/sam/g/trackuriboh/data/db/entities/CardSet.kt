package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class CardSet(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String?,
    val code: String?,
    val releaseDate: Date?
) : Parcelable
