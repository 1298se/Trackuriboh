package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.CardRarity
import sam.g.trackuriboh.data.db.entities.Condition
import sam.g.trackuriboh.data.db.entities.Printing
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {

    suspend fun upsertCardRarities(rarities: List<CardRarity>) =
        appDatabase.cardRarityDao().upsert(rarities)

    suspend fun upsertConditions(conditions: List<Condition>) =
        appDatabase.conditionDao().upsert(conditions)

    suspend fun upsertPrintings(printings: List<Printing>) =
        appDatabase.printingDao().upsert(printings)

    suspend fun getCardRarityByName(name: String): CardRarity? {
        return appDatabase.cardRarityDao().getCardRarityByName(name)
            ?: appDatabase.cardRarityDao().getCardRarityByName("Unconfirmed")
    }
}
