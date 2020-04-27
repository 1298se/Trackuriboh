package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagedList
import tang.song.edu.yugiohcollectiontracker.data.network.PagedListBoundaryCallbackResponse

abstract class BaseSearchViewModel<T> : ViewModel() {
    private val _queryLiveData = MutableLiveData<String?>()

    private val itemSearchResult: LiveData<PagedListBoundaryCallbackResponse<T>> = Transformations.switchMap(_queryLiveData) {
        liveData(viewModelScope.coroutineContext) {
            emit(searchSource(it ?: ""))
        }
    }

    protected val itemList: LiveData<PagedList<T>> = Transformations.switchMap(itemSearchResult) { it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(itemSearchResult) { it.networkErrors }

    /**
     * Search a repository based on a query string.
     */
    fun search(queryString: String?) {
        _queryLiveData.postValue(queryString?.trim())
    }

    abstract suspend fun searchSource(queryString: String): PagedListBoundaryCallbackResponse<T>
}