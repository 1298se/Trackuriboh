package sam.g.trackuriboh.data.db.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.CardRarity
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product

@Parcelize
data class ProductWithCardSet(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "setId",
        entityColumn = "id",
    )
    val cardSet: CardSet,
    @Relation(
        entity = CardRarity::class,
        parentColumn = "rarityId",
        entityColumn = "id"
    )
    val rarity: CardRarity,
) : Parcelable
