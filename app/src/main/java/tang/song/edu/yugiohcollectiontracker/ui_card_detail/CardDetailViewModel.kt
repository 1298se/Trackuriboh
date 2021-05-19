package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {
    var currentSelectedDetailsPage = 0

    suspend fun getCardDetailsById(cardId: Long) = cardRepository.getCardDetails(cardId)
}
