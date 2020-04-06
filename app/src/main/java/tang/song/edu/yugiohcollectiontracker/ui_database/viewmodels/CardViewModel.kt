package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardViewModel(cardRepository: CardRepository) : ViewModel() {
    val getCardList = cardRepository.getCardList()
}
