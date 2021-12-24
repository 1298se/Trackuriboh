package sam.g.trackuriboh.data.types

import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.R

@Parcelize
enum class ReminderType(override val resourceId: Int) : StringResourceEnum {
    AUCTION(R.string.reminder_type_auction),
    CLAIM_SALE(R.string.reminder_type_claim_sale),
    OTHER(R.string.reminder_type_other_event);
}
