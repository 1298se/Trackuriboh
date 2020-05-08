package tang.song.edu.yugiohcollectiontracker.ui_card_set_detail.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository

class CardSetDetailViewModel(private val cardRepository: CardRepository, private val cardSetRepository: CardSetRepository) : ViewModel() {
    fun getCardListBySet(setCode: String) = cardRepository.getCardListBySet(setCode)

    fun getCardSetByCode(setCode: String) = liveData(Dispatchers.IO + viewModelScope.coroutineContext) {
        emit(cardSetRepository.getCardSetByCode(setCode))
    }
}