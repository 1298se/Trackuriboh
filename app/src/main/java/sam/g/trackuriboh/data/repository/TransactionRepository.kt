package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.TransactionLocalCache
import sam.g.trackuriboh.data.db.entities.TransactionType
import sam.g.trackuriboh.data.db.entities.UserTransaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionLocalCache: TransactionLocalCache,
    private val userListRepository: UserListRepository,
) {
    suspend fun insertTransaction(userTransaction: UserTransaction) {
        val entry = userListRepository.getUserListEntry(userTransaction.listId, userTransaction.skuId)

        if (entry != null) {
            var quantity = entry.quantity
            var avgPurchasePrice = entry.avgPurchasePrice

            when (userTransaction.type) {
                TransactionType.PURCHASE -> {
                    quantity += userTransaction.quantity
                    avgPurchasePrice =
                        (avgPurchasePrice * entry.quantity) + (userTransaction.price * userTransaction.quantity) / (quantity)
                }
                TransactionType.SALE -> {
                   quantity -= userTransaction.quantity

                }
            }

            userListRepository.updateUserListEntry(entry.copy(avgPurchasePrice = avgPurchasePrice, quantity = quantity))
            transactionLocalCache.insertTransaction(userTransaction)
        }
    }

    fun getUserListEntryTransactions(listId: Long, skuId: Long) =
        transactionLocalCache.getUserListEntryTransactionsObservable(listId, skuId)
}
