package tang.song.edu.yugiohcollectiontracker.ui_search.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository

class SearchViewModel(cardRepository: CardRepository, cardSetRepository: CardSetRepository) : ViewModel() {
    private val queryLiveData = MutableLiveData<String>()

    val cardSearchResult: LiveData<PagedList<Card>> = Transformations.switchMap(queryLiveData) {
        cardRepository.search(it)
    }

    /**
     * Search a repository based on a query string.
     */
    fun search(queryString: String?) {
        queryLiveData.postValue(queryString)
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value
}