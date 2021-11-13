package sam.g.trackuriboh.ui_database.viewmodels

import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

abstract class BaseSearchViewModel<T : Any> : ViewModel() {

    private var query = MutableLiveData<String?>(null)

    private var searchResult: LiveData<PagingData<T>> = Transformations.switchMap(query) {
        searchSource(it).cachedIn(viewModelScope).asLiveData()
    }

    /**
     * Search a repository based on a query string.
     * We want to check against the previous query, because if it's the same, we don't want to reload the data
     */
    @MainThread
    fun search(query: String?) {
        if (query != this.query.value) {
            this.query.value = query
        }
    }

    fun currentQueryValue(): String? = query.value

    fun getSearchResult(): LiveData<PagingData<T>> = searchResult

    protected abstract fun searchSource(query: String?): Flow<PagingData<T>>
}