package tang.song.edu.trackuriboh.data.db.relations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardSetInfo(
    val setCode: String,
    val setName: String,
    val cardNumber: String,
    val rarity: String?,
    val releaseDate: String?,
    val price: String?
) : Parcelable
