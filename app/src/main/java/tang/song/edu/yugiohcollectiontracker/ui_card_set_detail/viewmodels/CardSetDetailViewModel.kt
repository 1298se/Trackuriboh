package tang.song.edu.yugiohcollectiontracker.ui_card_set_detail.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardSetDetailViewModel @ViewModelInject constructor(
    private val cardRepository: CardRepository,
) : ViewModel() {
    fun getCardListBySet(setName: String) = cardRepository.getCardListBySet(setName)
}