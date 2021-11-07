package sam.g.trackuriboh.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Condition::class,
            parentColumns = ["id"],
            childColumns = ["conditionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Sku(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val productId: Long,
    val printingId: Long?,
    val conditionId: Long?,
)
