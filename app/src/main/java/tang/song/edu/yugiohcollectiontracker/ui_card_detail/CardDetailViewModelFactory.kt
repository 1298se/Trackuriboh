package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tang.song.edu.yugiohcollectiontracker.data.repository.CardInventoryRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import javax.inject.Inject

class CardDetailViewModelFactory @Inject constructor(
    private val cardRepository: CardRepository,
    private val cardInventoryRepository: CardInventoryRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailViewModel::class.java)) {
            return CardDetailViewModel(
                cardRepository,
                cardInventoryRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}