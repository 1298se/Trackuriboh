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

    suspend fun insertCardRarities(rarities: List<CardRarity>) =
        appDatabase.cardRarityDao().insert(rarities)

    suspend fun insertConditions(conditions: List<Condition>) =
        appDatabase.conditionDao().insert(conditions)

    suspend fun insertPrintings(printings: List<Printing>) =
        appDatabase.printingDao().insert(printings)
}
