package sam.g.trackuriboh.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE INDEX index_Sku_productId ON Sku(productId)")
        database.execSQL("CREATE INDEX index_CardRarity_name ON CardRarity(name)")
    }
}