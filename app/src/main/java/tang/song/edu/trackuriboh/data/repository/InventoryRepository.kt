package tang.song.edu.trackuriboh.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import tang.song.edu.trackuriboh.DATABASE_PAGE_SIZE
import tang.song.edu.trackuriboh.data.db.CardInventoryLocalCache
import tang.song.edu.trackuriboh.data.db.entities.CardInventory
import tang.song.edu.trackuriboh.data.db.entities.Transaction
import tang.song.edu.trackuriboh.data.db.relations.CardInventoryWithTransactions
import tang.song.edu.trackuriboh.data.types.EditionType
import tang.song.edu.trackuriboh.data.types.PlatformType
import tang.song.edu.trackuriboh.data.types.TransactionType
import tang.song.edu.trackuriboh.ui_transaction_form.models.TransactionDataModel
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepository @Inject constructor(
    private val cardInventoryLocalCache: CardInventoryLocalCache
) {
    fun getInventoryList(): Flow<PagingData<CardInventory>> {
        val pagingSourceFactory = { cardInventoryLocalCache.getInventoryList() }

        return Pager(
            config = PagingConfig(pageSize = DATABASE_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow.flowOn(Dispatchers.IO)
    }

    suspend fun getInventoryWithTransactions(inventoryId: Long): CardInventoryWithTransactions {
        return cardInventoryLocalCache.getInventoryWithTransactions(inventoryId)

    }

    suspend fun insertTransaction(transactionData: TransactionDataModel): Long {
        var inventory = cardInventoryLocalCache.getInventory(transactionData.cardNumber!!, transactionData.rarity, transactionData.edition as EditionType)?.apply {
            lastTransaction = transactionData.date!!
        }

        if (inventory == null) {
            inventory = CardInventory(
                cardId = transactionData.cardId!!,
                cardName = transactionData.cardName!!,
                cardNumber = transactionData.cardNumber!!,
                lastTransaction = transactionData.date!!,
                cardImageURL = transactionData.cardImageURL,
                rarity = transactionData.rarity,
                edition = transactionData.edition as EditionType,
            )
        }

        when (transactionData.transactionType) {
            TransactionType.PURCHASE -> {
                inventory.curAvgPurchasePrice = calculateAveragePrice(inventory.curAvgPurchasePrice, inventory.quantity, transactionData.price?.toDouble(), transactionData.quantity?.toInt())
                inventory.quantity += transactionData.quantity?.toInt() ?: 0
            }
            TransactionType.SALE -> {
                inventory.profitAndLoss = calculateProfitAndLoss(inventory.curAvgPurchasePrice, transactionData.price?.toDouble(), transactionData.quantity?.toInt())
                inventory.soldQuantity += transactionData.quantity?.toInt() ?: 0
                inventory.quantity -= transactionData.quantity?.toInt() ?: 0
            }
        }

        val inventoryId = cardInventoryLocalCache.insertInventory(inventory)
        return cardInventoryLocalCache.insertTransaction(Transaction(
            inventoryId = inventoryId,
            transactionType = transactionData.transactionType as TransactionType,
            quantity = transactionData.quantity?.toInt(),
            date = transactionData.date,
            partyName = transactionData.partyName,
            trackingNumber = transactionData.tracking,
            price = transactionData.price?.toDouble(),
            platformType = transactionData.platformType as PlatformType
        ))
    }

    private fun calculateAveragePrice(curAverage: Double?, curQuantity: Int?, price: Double?, newQuantity: Int?): Double {
        return DecimalFormat("#.##").format(((curAverage ?: 0.toDouble()) * (curQuantity ?: 0) + (price ?: 0.toDouble()) * (newQuantity ?: 0)) / ((curQuantity ?: 0) + (newQuantity ?: 0))).toDouble()
    }

    private fun calculateProfitAndLoss(curAveragePurchasePrice: Double?, averageSoldPrice: Double?, soldQuantity: Int?): Double {
        return DecimalFormat("#.##").format(((averageSoldPrice ?: 0.toDouble()) - (curAveragePurchasePrice ?: 0.toDouble())) * (soldQuantity ?: 0)).toDouble()
    }

}