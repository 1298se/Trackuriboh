package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.relations.ProductWithSetAndSkuIds
import sam.g.trackuriboh.data.types.ProductType

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product WHERE Product.id = :productId")
    suspend fun getProductWithSkusById(productId: Long): ProductWithSetAndSkuIds?

    @Query("SELECT * FROM Product WHERE name LIKE :query AND type IS :productType ORDER BY name ASC")
    fun searchProductByName(query: String, productType: ProductType): PagingSource<Int, ProductWithSetAndSkuIds>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>): List<Long>

    @Query("DELETE FROM Product")
    suspend fun deleteTable()
}
