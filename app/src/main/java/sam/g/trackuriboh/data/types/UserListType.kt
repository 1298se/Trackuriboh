package sam.g.trackuriboh.data.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class UserListType : Parcelable {
    USER_LIST,
    // CHECKLIST(R.string.collection_type_checklist),
}
