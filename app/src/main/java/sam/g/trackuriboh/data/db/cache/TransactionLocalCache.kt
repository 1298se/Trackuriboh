package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.entities.Transaction
import sam.g.trackuriboh.data.db.entities.UserListEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionLocalCache @Inject constructor(
    private val appDatabase: AppDatabase,
) {
    suspend fun upsertTransactionAndUpdateUserListEntry(entry: UserListEntry, transaction: Transaction) =
        appDatabase.transactionDao().upsertTransactionAndUpdateUserListEntry(entry, transaction)
}
