package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardDetailViewModel(private val cardRepository: CardRepository) : ViewModel() {
    fun getCardById(cardId: Long) = liveData {
        emit(cardRepository.getCardById(cardId))
    }
}