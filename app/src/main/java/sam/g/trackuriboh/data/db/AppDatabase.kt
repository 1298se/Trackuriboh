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
        Reminder::class,
    ],
    version = 1
)
@TypeConverters(RoomConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun skuDao(): SkuDao
    abstract fun cardSetDao(): CardSetDao
    abstract fun cardRarityDao(): CardRarityDao
    abstract fun printingDao(): PrintingDao
    abstract fun conditionDao(): ConditionDao

    abstract fun reminderDao(): ReminderDao

    interface DatabaseEntity<out T> {
        fun toDatabaseEntity(): T
    }

    /**
     * We do this because [clearAllTables] doesn't work with coroutines - we can't monitor when it ends
      */
    suspend fun clearCardDatabaseTables() {
        skuDao().clearTable()
        productDao().clearTable()
        cardSetDao().clearTable()

        cardRarityDao().clearTable()
        printingDao().clearTable()
        conditionDao().clearTable()
    }

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private const val DATABASE_NAME = "cardDatabase.db"
        private const val DATABASE_FILE_PATH = "db/$DATABASE_NAME"

        operator fun invoke(application: Application) = instance
            ?: synchronized(this) {
                buildDatabase(
                    application
                ).also { instance = it }
            }

        private fun buildDatabase(application: Application) =
            Room.databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
                .createFromAsset(DATABASE_FILE_PATH)
                .build()
    }
}
