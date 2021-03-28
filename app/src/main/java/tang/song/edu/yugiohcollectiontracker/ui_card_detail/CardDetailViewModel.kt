package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardDetailViewModel @ViewModelInject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {
    var currentSelectedDetailsPage = 0

    suspend fun getCardDetailsById(cardId: Long) = cardRepository.getCardDetails(cardId)
}
