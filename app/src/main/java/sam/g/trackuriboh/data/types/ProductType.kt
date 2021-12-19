package sam.g.trackuriboh.data.types

import kotlinx.parcelize.Parcelize

@Parcelize
enum class ProductType(override val resourceId: Int) : StringResourceEnum {
    CARD(1),
    ;
}