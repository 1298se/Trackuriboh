package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardViewModel(cardRepository: CardRepository) : ViewModel() {
    val cardListResult = cardRepository.getAllCards().asLiveData()
}
