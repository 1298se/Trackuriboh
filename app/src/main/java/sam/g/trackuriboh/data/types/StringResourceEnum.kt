package sam.g.trackuriboh.data.types

import android.os.Parcelable

interface StringResourceEnum : Parcelable {
    val value: Int
    fun getResourceId(): Int
}
