package tang.song.edu.yugiohcollectiontracker.ui_database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.network.response.Resource

class CardViewModel(cardRepository: CardRepository) : ViewModel() {
    val allCards = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        emit(cardRepository.getAllCards())
    }
}
