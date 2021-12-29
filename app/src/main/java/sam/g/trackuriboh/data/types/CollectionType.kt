package sam.g.trackuriboh.data.types

import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.R

@Parcelize
enum class CollectionType(override val resourceId: Int) : StringResourceEnum {
    COLLECTION(R.string.collection_type_collection),
    // CHECKLIST(R.string.collection_type_checklist),
}
