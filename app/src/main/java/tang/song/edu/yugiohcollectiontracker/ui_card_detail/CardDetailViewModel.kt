package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import androidx.hilt.lifecycle.ViewModelInject
import tang.song.edu.yugiohcollectiontracker.BaseCardViewModel
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardDetailViewModel @ViewModelInject constructor(
    private val cardRepository: CardRepository) : BaseCardViewModel(cardRepository) {
}
