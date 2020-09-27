package tang.song.edu.yugiohcollectiontracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

open class BaseCardViewModel(private val cardRepository: CardRepository) : ViewModel() {
    fun getCardDetailsById(cardId: Long) = liveData {
        emit(cardRepository.getCardDetails(cardId))
    }
}