package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository
import javax.inject.Inject

class CardSetListViewModelFactory @Inject constructor(private val cardSetRepository: CardSetRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardSetListViewModel::class.java)) {
            return CardSetListViewModel(
                cardSetRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}