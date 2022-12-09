package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.TransactionLocalCache
import sam.g.trackuriboh.data.db.entities.Transaction
import sam.g.trackuriboh.data.db.entities.UserListEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionLocalCache: TransactionLocalCache
) {
    suspend fun upsertTransactionAndUpdateUserListEntry(entry: UserListEntry, transaction: Transaction) =
        transactionLocalCache.upsertTransactionAndUpdateUserListEntry(entry, transaction)
}