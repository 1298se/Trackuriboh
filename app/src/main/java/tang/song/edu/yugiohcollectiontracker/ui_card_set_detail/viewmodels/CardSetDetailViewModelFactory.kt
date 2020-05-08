package tang.song.edu.yugiohcollectiontracker.ui_card_set_detail.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository
import javax.inject.Inject

class CardSetDetailViewModelFactory @Inject constructor(private val cardRepository: CardRepository, private val cardSetRepository: CardSetRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardSetDetailViewModel::class.java)) {
            return CardSetDetailViewModel(
                cardRepository,
                cardSetRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}