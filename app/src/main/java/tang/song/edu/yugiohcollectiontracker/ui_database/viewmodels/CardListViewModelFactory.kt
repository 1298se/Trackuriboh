package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import javax.inject.Inject

class CardListViewModelFactory @Inject constructor(private val cardRepository: CardRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardListViewModel::class.java)) {
            return CardListViewModel(
                cardRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}