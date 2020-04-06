package tang.song.edu.yugiohcollectiontracker.ui_search.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
    private val cardRepository: CardRepository,
    private val cardSetRepository: CardSetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(cardRepository, cardSetRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}