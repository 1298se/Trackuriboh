package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.UserTransaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionLocalCache @Inject constructor(
    private val appDatabase: AppDatabase,
) {
    suspend fun insertTransaction(userTransaction: UserTransaction) =
        appDatabase.userTransactionDao().insert(userTransaction)

    fun getUserListEntryTransactionsObservable(listId: Long, skuId: Long) =
        appDatabase.userTransactionDao().getUserEntryTransactionsObservable(listId, skuId)
}