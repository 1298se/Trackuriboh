package tang.song.edu.yugiohcollectiontracker.data.types

import tang.song.edu.yugiohcollectiontracker.R

enum class PlatformType(val value: Int) : StringResourceEnum {
    EBAY(1),
    FACEBOOK(2),
    OTHER(3);

    companion object {
        private val map = values().associateBy(PlatformType::value)
        fun fromInt(platformType: Int?) = map[platformType]
    }

    override fun getResourceId(): Int = when(this) {
        EBAY -> R.string.lbl_ebay
        FACEBOOK -> R.string.lbl_facebook
        OTHER -> R.string.lbl_other
    }
}