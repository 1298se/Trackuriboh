package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.relations.ProductWithSetInfo
import sam.g.trackuriboh.data.db.relations.ProductWithSkus
import sam.g.trackuriboh.data.types.ProductType

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product WHERE id = :productId")
    suspend fun getProductById(productId: Long): ProductWithSkus?

    @Query("SELECT * FROM Product WHERE name LIKE :query AND type IS :productType ORDER BY name ASC")
    fun searchProductByName(query: String, productType: ProductType): PagingSource<Int, ProductWithSetInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>): List<Long>

    @Query("DELETE FROM Product")
    suspend fun deleteTable()
}
