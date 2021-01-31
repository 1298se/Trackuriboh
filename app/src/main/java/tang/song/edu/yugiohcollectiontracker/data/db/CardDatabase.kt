package tang.song.edu.yugiohcollectiontracker.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tang.song.edu.yugiohcollectiontracker.data.db.converters.RoomConverter
import tang.song.edu.yugiohcollectiontracker.data.db.dao.*
import tang.song.edu.yugiohcollectiontracker.data.db.entities.*

@Database(
    entities = [Card::class, CardSet::class, CardXCardSetRef::class, CardInventory::class, Transaction::class],
    version = 1
)
@TypeConverters(RoomConverter::class)
abstract class CardDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun cardSetDao(): CardSetDao
    abstract fun cardXCardSetDao(): CardXCardSetDao

    abstract fun cardInventoryDao(): CardInventoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun cardInventoryXTransactionDao(): CardInventoryXTransactionDao

    companion object {
        @Volatile
        private var instance: CardDatabase? = null

        operator fun invoke(application: Application) = instance
            ?: synchronized(this) {
                buildDatabase(
                    application
                ).also { instance = it }
            }

        private fun buildDatabase(application: Application) = Room.databaseBuilder(
            application,
            CardDatabase::class.java,
            "cardDatabase.db"
        ).build()
    }
}
