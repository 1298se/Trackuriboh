package tang.song.edu.yugiohcollectiontracker.ui_card_set_detail.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardSetDetailViewModel @ViewModelInject constructor(
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