package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import javax.inject.Inject

class CardViewModelFactory @Inject constructor (private val cardRepository: CardRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            return CardViewModel(
                cardRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}