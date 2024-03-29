package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.types.UserListType
import java.util.*

@Parcelize
@Entity
data class UserList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val creationDate: Date,
    val type: UserListType,
) : Parcelable