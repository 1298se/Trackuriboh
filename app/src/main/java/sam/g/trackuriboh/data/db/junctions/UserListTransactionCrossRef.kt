package sam.g.trackuriboh.data.db.junctions

import androidx.room.Entity

@Entity(primaryKeys = ["listId", "transactionId"])
data class UserListTransactionCrossRef(
    val listId: Long,
    val transactionId: Long
)