package sam.g.trackuriboh.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sam.g.trackuriboh.data.db.converters.RoomConverter
import sam.g.trackuriboh.data.db.dao.*
import sam.g.trackuriboh.data.db.entities.*

@Database(
    entities = [
        Product::class,
        Sku::class,
        CardSet::class,
        CardRarity::class,
        Printing::class,
        Condition::class,
        CardInventory::class,
        Transaction::class,
               ],
    version = 1
)
@TypeConverters(RoomConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun productSkuDao(): SkuDao
    abstract fun cardSetDao(): CardSetDao
    abstract fun cardRarityDao(): CardRarityDao
    abstract fun printingDao(): PrintingDao
    abstract fun conditionDao(): ConditionDao

    abstract fun cardInventoryDao(): CardInventoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun cardInventoryXTransactionDao(): CardInventoryXTransactionDao

    interface DatabaseEntity<out T> {
        fun toDatabaseEntity(): T
    }

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(application: Application) = instance
            ?: synchronized(this) {
                buildDatabase(
                    application
                ).also { instance = it }
            }

        private fun buildDatabase(application: Application) = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "cardDatabase.db"
        ).build()
    }
}
