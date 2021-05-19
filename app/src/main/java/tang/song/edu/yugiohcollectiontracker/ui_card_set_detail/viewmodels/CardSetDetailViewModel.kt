package tang.song.edu.yugiohcollectiontracker.ui_card_set_detail.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class CardSetDetailViewModel @Inject constructor(
    private val cardRepository: CardRepository,
) : ViewModel() {
    var currentCardListResult: Flow<PagingData<Card>>? = null

    fun getCardListBySet(setName: String): Flow<PagingData<Card>> {
        val lastResult = currentCardListResult

        if (lastResult != null) {
            return lastResult
        }

        val newResult = cardRepository.getCardListBySet(setName)
        currentCardListResult = newResult
        return newResult
    }
}