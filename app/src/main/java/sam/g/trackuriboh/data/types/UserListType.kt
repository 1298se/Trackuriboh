package sam.g.trackuriboh.data.types

import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.R

@Parcelize
enum class UserListType(override val resourceId: Int) : StringResourceEnum {
    USER_LIST(R.string.user_list_type_list),
    // CHECKLIST(R.string.collection_type_checklist),
}
