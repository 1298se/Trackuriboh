package sam.g.trackuriboh.ui_database

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import sam.g.trackuriboh.BaseFragment
import sam.g.trackuriboh.ui_database.viewmodels.BaseSearchViewModel

/**
 * Base Fragment to handle basic search list logic, such as observing a search result LiveData
 * and submitting the data to the adapter
 */
abstract class BaseSearchListFragment<T : Any> : BaseFragment() {

    init {
        lifecycleScope.launchWhenStarted {
            getAdapter().loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { getListView().scrollToPosition(0) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewModel().getSearchResult().observe(viewLifecycleOwner) {
            getAdapter().submitData(lifecycle, it)
        }
    }

    fun search(query: String?) {
        getViewModel().search(query)
    }

    fun lastQueryValue() = getViewModel().currentQueryValue()

    protected abstract fun getViewModel(): BaseSearchViewModel<T>
    protected abstract fun getListView(): RecyclerView
    protected abstract fun getAdapter(): PagingDataAdapter<T, out RecyclerView.ViewHolder>
    // protected abstract fun submitData(pagingData: PagingData<T>)
}
