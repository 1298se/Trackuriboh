package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.types.ReminderType
import java.util.*

@Parcelize
@Entity
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: ReminderType,
    val link: String?,
    val date: Date,
) : Parcelable
