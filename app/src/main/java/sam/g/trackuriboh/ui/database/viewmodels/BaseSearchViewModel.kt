package sam.g.trackuriboh.ui.database.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.utils.SingleEvent

abstract class BaseSearchViewModel<T : Any> : ViewModel() {

    private val searchQuery = MutableLiveData<String?>(null)

    val searchResult: LiveData<PagingData<T>> = Transformations.switchMap(searchQuery) {
        searchSource(it).cachedIn(viewModelScope).asLiveData()
    }

    val shouldScrollToTop: LiveData<SingleEvent<Boolean>>
        get() = _shouldScrollToTop

    private val _shouldScrollToTop = MutableLiveData(SingleEvent(false))

    /**
     * Search a repository based on a query string.
     * We want to check against the previous query, because if it's the same, we don't want to reload the data
     * and scroll back to the top of the list
     */
    fun search(query: String?) {
        if (query != searchQuery.value) {
            searchQuery.value = query
            _shouldScrollToTop.value = _shouldScrollToTop.value?.copy(true)
        }
    }

    fun currentQueryValue() = searchQuery.value

    protected abstract fun searchSource(query: String?): Flow<PagingData<T>>
}