package tang.song.edu.yugiohcollectiontracker.data.types

import android.os.Parcelable

interface StringResourceEnum : Parcelable {
    val value: Int
    fun getResourceId(): Int
}
