package sam.g.trackuriboh.data.db

import android.app.Application
import androidx.room.*
import sam.g.trackuriboh.data.db.converters.RoomConverter
import sam.g.trackuriboh.data.db.dao.*
import sam.g.trackuriboh.data.db.entities.*
import sam.g.trackuriboh.data.db.migrations.MIGRATION_1_2
import sam.g.trackuriboh.data.db.migrations.MIGRATION_2_3

@Database(
    version = 4,
    entities = [
        Product::class,
        Sku::class,
        CardSet::class,
        CardRarity::class,
        Printing::class,
        Condition::class,
        Reminder::class,
        UserList::class,
        UserListEntry::class,
        Inventory::class,
        InventoryTransaction::class,
    ],
    autoMigrations = [
        AutoMigration (from = 3, to = 4)
    ]
)
@TypeConverters(RoomConverter::class)
abstract class AppDatabase : RoomDatabase() {

    // NOTE: DAOs automatically switches thread when using suspend
    abstract fun productDao(): ProductDao
    abstract fun skuDao(): SkuDao
    abstract fun cardSetDao(): CardSetDao
    abstract fun cardRarityDao(): CardRarityDao
    abstract fun printingDao(): PrintingDao
    abstract fun conditionDao(): ConditionDao

    abstract fun reminderDao(): ReminderDao

    abstract fun userListDao(): UserListDao
    abstract fun userListEntryDao(): UserListEntryDao

    abstract fun inventoryDao(): InventoryDao
    abstract fun inventoryTransactionDao(): InventoryTransactionDao

    /**
     * We do this because [clearAllTables] doesn't work with coroutines - we can't monitor when it ends
     */
    suspend fun clearCardDatabaseTables() {
        userListEntryDao().clearTable()
        userListDao().clearTable()

        skuDao().clearTable()
        productDao().clearTable()
        cardSetDao().clearTable()

        cardRarityDao().clearTable()
        printingDao().clearTable()
        conditionDao().clearTable()

        inventoryDao().clearTable()
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
            Room.databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME).apply {
                //if (!BuildConfig.DEBUG) {
                    createFromAsset(DATABASE_FILE_PATH)
                //}
            }.addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
    }
}
