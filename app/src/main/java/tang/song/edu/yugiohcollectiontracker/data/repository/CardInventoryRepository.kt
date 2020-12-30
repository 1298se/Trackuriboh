package tang.song.edu.yugiohcollectiontracker.data.repository

import tang.song.edu.yugiohcollectiontracker.data.db.CardInventoryLocalCache
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Transaction
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardInventoryWithTransactions
import tang.song.edu.yugiohcollectiontracker.data.models.TransactionType
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardInventoryRepository @Inject constructor(
    private val cardInventoryLocalCache: CardInventoryLocalCache
) {
    suspend fun getInventoryWithTransactions(inventoryId: Long): CardInventoryWithTransactions {
        return cardInventoryLocalCache.getInventoryWithTransactions(inventoryId)

    }

    suspend fun insertTransaction(cardInventory: CardInventory, transaction: Transaction): Long {
        val inventory = cardInventoryLocalCache.getInventory(cardInventory.cardNumber, cardInventory.rarity) ?: cardInventory

        val inventoryId = cardInventoryLocalCache.insertInventory(inventory.apply {
            when (transaction.transactionType) {
                TransactionType.PURCHASE -> {
                    currentAvgPurchasePrice = calculateAveragePrice(cardInventory.currentAvgPurchasePrice, cardInventory.quantity, transaction.amount, transaction.quantity)
                    quantity += transaction.quantity ?: 0
                }
                TransactionType.SALE -> {
                    avgSalePrice = calculateAveragePrice(cardInventory.avgSalePrice, cardInventory.soldQuantity, transaction.amount, transaction.quantity)
                    quantity -= transaction.quantity ?: 0
                    soldQuantity += transaction.quantity ?: 0
                }
                else -> return@apply
            }
        })

        return cardInventoryLocalCache.insertTransaction(transaction.apply{
            this.inventoryId = inventoryId
        })
    }

    private fun calculateAveragePrice(curAverage: Double?, curQuantity: Int?, amount: Double?, newQuantity: Int?): Double {
        return DecimalFormat("#.##").format(((curAverage ?: 0.toDouble()) * (curQuantity ?: 0) + (amount ?: 0.toDouble()) * (newQuantity ?: 0)) / ((curQuantity ?: 0) + (newQuantity ?: 0))).toDouble()
    }

}