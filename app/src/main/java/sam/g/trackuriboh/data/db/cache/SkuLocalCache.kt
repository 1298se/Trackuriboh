package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkuLocalCache @Inject constructor(
    private val appDatabase: AppDatabase,
) {

    suspend fun getSkuIdsPaginated(offset: Int, limit: Int) =
        appDatabase.skuDao().getSkuIdsPaginated(offset, limit)
}
