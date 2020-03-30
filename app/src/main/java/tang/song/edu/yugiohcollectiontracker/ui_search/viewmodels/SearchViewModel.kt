package tang.song.edu.yugiohcollectiontracker.ui_search.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class SearchViewModel(cardRepository: CardRepository) : ViewModel() {
    val cardSearchResult = liveData {
        emit(cardRepository.getAllCards())
    }
}