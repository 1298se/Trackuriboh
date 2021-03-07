package tang.song.edu.yugiohcollectiontracker.ui_database

import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.BaseSearchViewModel

abstract class BaseSearchListFragment<T : Any> : BaseFragment() {
    private var searchJob: Job? = null

    init {
        lifecycleScope.launchWhenStarted {
            getAdapter().loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { getListView().scrollToPosition(0) }
        }
    }

    fun updateSearchList(newText: String?) {
        (newText ?: "").trim().let {
            search(it)
        }
    }

    fun lastQueryValue() = getViewModel().currentQueryValue()

    protected fun search(queryString: String?) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            getViewModel().search(queryString).collectLatest {
                submitData(it)
            }
        }
    }

    protected abstract fun getViewModel(): BaseSearchViewModel<T>
    protected abstract fun getListView(): RecyclerView
    protected abstract fun getAdapter(): PagingDataAdapter<T, out RecyclerView.ViewHolder>
    protected abstract suspend fun submitData(pagingData: PagingData<T>)
}
