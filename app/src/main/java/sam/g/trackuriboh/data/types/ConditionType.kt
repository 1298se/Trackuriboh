package sam.g.trackuriboh.data.types

import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.R

@Parcelize
enum class ConditionType(override val value: Int) : StringResourceEnum {
    NEAR_MINT(1),
    VERY_LIGHTLY_PLAYED(2),
    LIGHTLY_PLAYED(3),
    MODERATELY_PLAYED(4),
    HEAVILY_PLAYED(5),
    DAMAGED(6);

    companion object {
        private val map = values().associateBy(ConditionType::value)
        fun fromInt(editionType: Int?) = map[editionType]
    }

    override fun getResourceId(): Int = when(this) {
        NEAR_MINT -> R.string.lbl_near_mint
        VERY_LIGHTLY_PLAYED -> R.string.lbl_lightly_played
        LIGHTLY_PLAYED -> R.string.lbl_very_lightly_played
        MODERATELY_PLAYED -> R.string.lbl_moderately_played
        HEAVILY_PLAYED -> R.string.lbl_heavily_played
        DAMAGED -> R.string.lbl_damaged
    }
}
