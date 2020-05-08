package tang.song.edu.yugiohcollectiontracker.data.db.relations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardSetInfo(
    val setCode: String,
    val setName: String,
    val cardNumber: String,
    val rarity: String?,
    val releaseDate: String?,
    val price: String?
) : Parcelable
