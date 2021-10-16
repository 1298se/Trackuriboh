package tang.song.edu.trackuriboh.data.types

import android.os.Parcelable

interface StringResourceEnum : Parcelable {
    val value: Int
    fun getResourceId(): Int
}
