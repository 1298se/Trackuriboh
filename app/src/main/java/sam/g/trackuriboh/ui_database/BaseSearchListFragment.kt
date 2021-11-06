package sam.g.trackuriboh.ui_database

import android.os.Bundle
import android.view.View
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.g.trackuriboh.BaseFragment
import sam.g.trackuriboh.ui_database.viewmodels.BaseSearchViewModel

/**
 * Base Fragment to handle basic search list logic, such as observing a search result LiveData
 * and submitting the data to the adapter
 */
abstract class BaseSearchListFragment<T : Any> : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewModel().getSearchResult().observe(viewLifecycleOwner) {
            getAdapter().submitData(lifecycle, it)
            getListView().scrollToPosition(0)
        }
    }

    fun updateSearchList(newText: String?) {
        search((newText ?: "").trim())
    }

    fun lastQueryValue() = getViewModel().currentQueryValue()

    protected fun search(queryString: String?) {
        getViewModel().search(queryString)
    }

    protected abstract fun getViewModel(): BaseSearchViewModel<T>
    protected abstract fun getListView(): RecyclerView
    protected abstract fun getAdapter(): PagingDataAdapter<T, out RecyclerView.ViewHolder>
}
