package tang.song.edu.trackuriboh.data.types

import kotlinx.parcelize.Parcelize
import tang.song.edu.trackuriboh.R

@Parcelize
enum class EditionType(override val value: Int) : StringResourceEnum {
    FIRST_EDITION(1),
    LIMITED_EDITION(2),
    UNLIMITED(3);

    companion object {
        private val map = values().associateBy(EditionType::value)
        fun fromInt(editionType: Int?) = map[editionType]
    }

    override fun getResourceId(): Int = when(this) {
        FIRST_EDITION -> R.string.lbl_first_edition
        LIMITED_EDITION -> R.string.lbl_limited_edition
        UNLIMITED -> R.string.lbl_unlimited_edition
    }
}