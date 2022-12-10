package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.TransactionLocalCache
import sam.g.trackuriboh.data.db.entities.UserTransaction
import sam.g.trackuriboh.data.db.entities.UserListEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionLocalCache: TransactionLocalCache
) {
    suspend fun upsertTransactionAndUpdateUserListEntry(entry: UserListEntry, userTransaction: UserTransaction) =
        transactionLocalCache.upsertTransactionAndUpdateUserListEntry(entry, userTransaction)
}