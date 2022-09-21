package sam.g.trackuriboh.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `CardSet`")
        database.execSQL("DROP TABLE `Product`")
        database.execSQL("DROP TABLE `CardRarity`")
        database.execSQL("CREATE TABLE `CardRarity` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))")
        database.execSQL("CREATE TABLE `CardSet` (`id` INTEGER NOT NULL, `name` TEXT, `code` TEXT, `releaseDate` INTEGER, `modifiedDate` INTEGER, PRIMARY KEY(`id`))")
        database.execSQL("CREATE TABLE `Product` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `cleanName` TEXT NOT NULL, `type` TEXT NOT NULL, `imageUrl` TEXT, `setId` INTEGER, `number` TEXT, `rarityId` INTEGER, `attribute` TEXT, `cardType` TEXT, `attack` INTEGER, `defense` INTEGER, `description` TEXT, PRIMARY KEY(`id`), FOREIGN KEY(`setId`) REFERENCES `CardSet`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`rarityId`) REFERENCES `CardRarity`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
    }
}