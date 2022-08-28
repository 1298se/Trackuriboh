package sam.g.trackuriboh.ui.search

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.utils.DATABASE_ASSET_CREATION_DATE
import sam.g.trackuriboh.workers.DatabaseUpdateWorker
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DatabaseExploreViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val cardSetRepository: CardSetRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {
    val recentCardSetsWithProducts = liveData {
        emit(cardSetRepository.getRecentSetsWithCards(5, 10))
    }

    val databaseStatusInfo = liveData {
        val lastUpdateDate = Date(sharedPreferences.getLong(DatabaseUpdateWorker.DATABASE_LAST_UPDATED_DATE, DATABASE_ASSET_CREATION_DATE))
        val totalCardSetCount = cardSetRepository.getTotalCardSetCount()
        val totalCardCount = productRepository.getTotalCardCount()

        emit(DatabaseStatusView.DatabaseStatusInfo(
            lastUpdated = lastUpdateDate,
            totalCardSetCount = totalCardSetCount,
            totalCardCount = totalCardCount,
        ))
    }
}