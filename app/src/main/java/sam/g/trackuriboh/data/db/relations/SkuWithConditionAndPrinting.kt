package sam.g.trackuriboh.data.db.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.Condition
import sam.g.trackuriboh.data.db.entities.Printing
import sam.g.trackuriboh.data.db.entities.Sku

@Parcelize
data class SkuWithConditionAndPrinting(
    @Embedded val sku: Sku,
    @Relation(
        parentColumn = "printingId",
        entityColumn = "id",
    )
    val printing: Printing?,
    @Relation(
        parentColumn = "conditionId",
        entityColumn = "id",
    )
    val condition: Condition?,
) : Parcelable
