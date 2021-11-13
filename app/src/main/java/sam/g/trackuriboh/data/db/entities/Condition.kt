package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Condition(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String?,
    val abbreviation: String?,
    // The order number that this Condition should be relative to other Conditions
    val order: Long?,
) : Parcelable
