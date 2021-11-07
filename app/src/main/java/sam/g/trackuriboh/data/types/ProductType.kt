package sam.g.trackuriboh.data.types

import kotlinx.parcelize.Parcelize

@Parcelize
enum class ProductType(override val value: Int) : StringResourceEnum {
    CARD(1),
    ;

    companion object {
        private val map = values().associateBy(ProductType::value)
        fun fromInt(platformType: Int?) = map[platformType]
    }

    override fun getResourceId(): Int = -1
}