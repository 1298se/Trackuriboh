package tang.song.edu.yugiohcollectiontracker.ui_database

import android.os.Bundle
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.yugiohcollectiontracker.BaseFragment

abstract class BaseSearchListFragment<T> : BaseFragment() {
    protected var mQueryString: String? = null

    companion object {
        const val ARGS_QUERY_STRING = "ARGS_QUERY_STRING"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mQueryString = it.getString(ARGS_QUERY_STRING)
        }
    }

    fun onQueryTextChange(newText: String?) {
        newText?.trim().let {
            getListView().scrollToPosition(0)
            search(it)
            submitList(null)
        }
    }

    protected abstract fun search(queryText: String?)
    protected abstract fun getListView(): RecyclerView
    protected abstract fun submitList(list: PagedList<T>?)
}
