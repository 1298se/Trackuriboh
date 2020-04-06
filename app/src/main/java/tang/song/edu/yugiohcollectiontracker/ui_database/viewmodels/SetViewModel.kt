package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository

class SetViewModel(cardSetRepository: CardSetRepository) : ViewModel() {
    val getCardSetList = cardSetRepository.getCardSetList()
}
