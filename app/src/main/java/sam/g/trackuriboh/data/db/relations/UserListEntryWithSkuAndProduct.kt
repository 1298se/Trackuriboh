package sam.g.trackuriboh.data.db.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.entities.UserListEntry

@Parcelize
data class UserListEntryWithSkuAndProduct(
    @Embedded val entry: UserListEntry,

    @Relation(
        entity = Sku::class,
        parentColumn = "skuId",
        entityColumn = "id",
    )
    val skuWithConditionAndPrintingAndProduct: SkuWithConditionAndPrintingAndProduct,
) : Parcelable
