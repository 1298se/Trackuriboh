package sam.g.trackuriboh.ui_database.viewmodels

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

abstract class BaseSearchViewModel<T : Any> : ViewModel() {
    private var query = MutableLiveData<String?>(null)

    private var searchResult: LiveData<PagingData<T>> = Transformations.switchMap(query) {
        searchSource(it).asLiveData()
    }

    /**
     * Search a repository based on a query string.
     */
    @MainThread
    @WorkerThread
    fun search(queryString: String?) = query.postValue(queryString)

    fun currentQueryValue(): String? = query.value

    fun getSearchResult(): LiveData<PagingData<T>> = searchResult

    protected abstract fun searchSource(queryString: String?): Flow<PagingData<T>>
}