package tang.song.edu.yugiohcollectiontracker.ui_database

import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.BaseSearchViewModel

abstract class BaseSearchListFragment : BaseFragment() {

    fun onQueryTextChange(newText: String?) {
        newText?.trim().let {
            getListView().scrollToPosition(0)
            search(it)
            clearList()
        }
    }

    fun lastQueryValue() = getViewModel().lastQueryValue()

    private fun search(queryText: String?) {
        getViewModel().search(queryText)
    }

    protected abstract fun getViewModel(): BaseSearchViewModel<*>
    protected abstract fun getListView(): RecyclerView
    protected abstract fun clearList()
}
