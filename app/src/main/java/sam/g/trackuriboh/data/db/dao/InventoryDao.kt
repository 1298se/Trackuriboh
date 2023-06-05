package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.Inventory
import sam.g.trackuriboh.data.db.relations.InventoryWithSkuMetadata
import sam.g.trackuriboh.data.db.relations.InventoryWithSkuMetadataAndTransactions

@Dao
interface InventoryDao : BaseDao<Inventory> {
    @Transaction
    @Query("SELECT * FROM Inventory")
    fun getInventoriesWithSkuMetadataAndTransactionsObservable(): Flow<List<InventoryWithSkuMetadataAndTransactions>>

    @Transaction
    @Query("SELECT * FROM Inventory WHERE Inventory.id = :inventoryId")
    fun getInventoryWithSkuMetadataAndTransactionsObservable(inventoryId: Long): Flow<InventoryWithSkuMetadataAndTransactions?>

    @Query("SELECT * FROM Inventory WHERE Inventory.skuId = :skuId")
    suspend fun getBySkuId(skuId: Long): Inventory?

    @Query("SELECT * FROM Inventory WHERE Inventory.id = :inventoryId")
    suspend fun get(inventoryId: Long): Inventory?

    @Transaction
    @Query("SELECT * FROM Inventory WHERE Inventory.id = :inventoryId")
    suspend fun getInventoryWithSkuMetadata(inventoryId: Long): InventoryWithSkuMetadata?

    @Query("DELETE FROM Inventory WHERE Inventory.id = :inventoryId")
    suspend fun deleteInventory(inventoryId: Long)


    @Query("DELETE FROM Inventory")
    suspend fun clearTable()
}
