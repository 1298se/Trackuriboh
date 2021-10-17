package sam.g.trackuriboh.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sam.g.trackuriboh.data.db.converters.RoomConverter
import sam.g.trackuriboh.data.db.dao.*
import sam.g.trackuriboh.data.db.entities.Card
import sam.g.trackuriboh.data.db.entities.CardInventory
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Transaction

@Database(
    entities = [Card::class, CardSet::class, CardInventory::class, Transaction::class],
    version = 1
)
@TypeConverters(RoomConverter::class)
abstract class CardDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun cardSetDao(): CardSetDao

    abstract fun cardInventoryDao(): CardInventoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun cardInventoryXTransactionDao(): CardInventoryXTransactionDao

    interface DatabaseEntity<out T> {
        fun toDatabaseEntity(): T
    }

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
