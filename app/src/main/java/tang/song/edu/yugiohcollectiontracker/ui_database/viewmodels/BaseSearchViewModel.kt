package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import android.text.TextUtils
import androidx.lifecycle.*
import androidx.paging.PagedList
import tang.song.edu.yugiohcollectiontracker.data.network.PagedListBoundaryCallbackResponse

abstract class BaseSearchViewModel<T> : ViewModel() {
    private val _queryLiveData = MutableLiveData<String>()

    private val itemSearchResult: LiveData<PagedListBoundaryCallbackResponse<T>> = Transformations.switchMap(_queryLiveData) {
        liveData(viewModelScope.coroutineContext) {
            if (TextUtils.isEmpty(it)) {
                emit(totalListSource())
            } else {
                emit(searchSource(it))
            }
        }
    }

    private val itemTotalResult: LiveData<PagedListBoundaryCallbackResponse<T>> = liveData {
        emit(totalListSource())
    }

    private val _itemListResult: MediatorLiveData<PagedListBoundaryCallbackResponse<T>> = MediatorLiveData()

    protected val itemList: LiveData<PagedList<T>> = Transformations.switchMap(_itemListResult) { it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(_itemListResult) { it.networkErrors }

    init {
        _itemListResult.addSource(itemTotalResult) {
            _itemListResult.value = it
        }

        _itemListResult.addSource(itemSearchResult) {
            _itemListResult.value = it
        }
    }

    /**
     * Search a repository based on a query string.
     */
    fun search(queryString: String?) {
        _queryLiveData.postValue(queryString?.trim())
    }

    abstract suspend fun totalListSource(): PagedListBoundaryCallbackResponse<T>
    abstract suspend fun searchSource(queryString: String): PagedListBoundaryCallbackResponse<T>
}