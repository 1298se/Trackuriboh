package sam.g.trackuriboh.ui_database

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import sam.g.trackuriboh.ui_database.viewmodels.BaseSearchViewModel
import sam.g.trackuriboh.utils.addItemDecorationIfExists

/**
 * Base Fragment to handle basic search list logic, such as observing a search result LiveData
 * and submitting the data to the adapter
 */
abstract class BaseSearchListFragment<T : Any> : Fragment() {
    private val mItemDecoration by lazy {
        getItemDecorator()
    }
    // Handles scrolling to the top of the list. If no new query is submitted, then
    // we don't scroll to the top
    private var mShouldScrollToTop = false

    init {
        lifecycleScope.launchWhenStarted {
            getAdapter().loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    if (mShouldScrollToTop) {
                        getListView().scrollToPosition(0)
                        mShouldScrollToTop = false
                    }
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewModel().getSearchResult().observe(viewLifecycleOwner) { pagingData ->
            getAdapter().submitData(lifecycle, pagingData)

            mItemDecoration?.let {
                getListView().addItemDecorationIfExists(it)
            }
        }

        getViewModel().shouldScrollToTop.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.handleEvent()?.let { mShouldScrollToTop = it }
        }
    }

    fun search(query: String?) {
        getViewModel().search(query)
    }

    fun lastQueryValue() = getViewModel().currentQueryValue()

    protected abstract fun getViewModel(): BaseSearchViewModel<T>
    protected abstract fun getListView(): RecyclerView
    protected abstract fun getAdapter(): PagingDataAdapter<T, out RecyclerView.ViewHolder>
    protected open fun getItemDecorator(): RecyclerView.ItemDecoration? = null
}
