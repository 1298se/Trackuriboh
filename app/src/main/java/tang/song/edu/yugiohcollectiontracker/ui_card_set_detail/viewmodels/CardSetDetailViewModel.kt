package tang.song.edu.yugiohcollectiontracker.ui_card_set_detail.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository

class CardSetDetailViewModel @ViewModelInject constructor(
    private val cardRepository: CardRepository,
    private val cardSetRepository: CardSetRepository,
) : ViewModel() {
    fun getCardListBySet(setName: String) = cardRepository.getCardListBySet(setName)

    fun getCardSet(setName: String) = liveData(Dispatchers.IO + viewModelScope.coroutineContext) {
        emit(cardSetRepository.getCardSet(setName))
    }
}