package sam.g.trackuriboh.data.db.relations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.UserTransaction
import sam.g.trackuriboh.data.db.entities.UserListEntry

@Parcelize
data class UserListEntryWithSkuAndProductAndTransactions(
    val entry: UserListEntry,
    val skuWithConditionAndPrintingAndProduct: SkuWithConditionAndPrintingAndProduct,
    val userTransactions: List<UserTransaction>
) : Parcelable
