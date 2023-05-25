package sam.g.trackuriboh.data.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.R

@Parcelize
enum class ReminderType : Parcelable {
    AUCTION,
    CLAIM_SALE,
    LISTING,
    OTHER;

    fun getDisplayStringRes() =
        when (this) {
            AUCTION -> R.string.reminder_type_auction
            CLAIM_SALE -> R.string.reminder_type_claim_sale
            LISTING -> R.string.reminder_type_listing
            OTHER -> R.string.reminder_type_other_event
        }
}
