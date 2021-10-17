package sam.g.trackuriboh.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

abstract class BaseSearchViewModel<T : Any> : ViewModel() {
    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<T>>? = null

    /**
     * Search a repository based on a query string.
     */
    fun search(queryString: String?): Flow<PagingData<T>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }

        currentQueryValue = queryString
        val newSearchResult = searchSource(queryString).cachedIn(viewModelScope)
        currentSearchResult = newSearchResult
        return newSearchResult
    }

    fun currentQueryValue(): String? = currentQueryValue

    protected abstract fun searchSource(queryString: String?): Flow<PagingData<T>>
}