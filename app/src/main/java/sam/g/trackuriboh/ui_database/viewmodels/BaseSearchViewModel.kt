package sam.g.trackuriboh.ui_database.viewmodels

import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.utils.SingleEvent

abstract class BaseSearchViewModel<T : Any> : ViewModel() {

    private var query = MutableLiveData<String?>(null)

    private var searchResult: LiveData<PagingData<T>> = Transformations.switchMap(query) {
        searchSource(it).cachedIn(viewModelScope).asLiveData()
    }

    val shouldScrollToTop: LiveData<SingleEvent<Boolean>>
        get() = _shouldScrollToTop

    private val _shouldScrollToTop = MutableLiveData(SingleEvent(false))


    /**
     * Search a repository based on a query string.
     * We want to check against the previous query, because if it's the same, we don't want to reload the data
     */
    @MainThread
    fun search(query: String?) {
        if (query != this.query.value) {
            this.query.value = query
            _shouldScrollToTop.value = _shouldScrollToTop.value?.copy(true)
        }
    }

    fun currentQueryValue(): String? = query.value

    fun getSearchResult(): LiveData<PagingData<T>> = searchResult

    protected abstract fun searchSource(query: String?): Flow<PagingData<T>>
}