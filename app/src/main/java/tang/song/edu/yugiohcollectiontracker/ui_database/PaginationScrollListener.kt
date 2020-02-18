package tang.song.edu.yugiohcollectiontracker.ui_database

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {
    private val visibleThreshold = 10
    private var currentPage = 0
    private var isLoading = true
    private var startingPageIndex = 1

    private var totalItemCount = 0
    private var previousTotalItemCount = 0
    private var lastVisibleItemPosition = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        totalItemCount = layoutManager.itemCount
        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

        // from filter or searching
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
        }

        // When items have loaded
        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false
            previousTotalItemCount = totalItemCount
        }

        if (!isLoading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++
            loadMore(currentPage, recyclerView)
            isLoading = true
        }
    }

    abstract fun loadMore(pageNum: Int, recyclerView: RecyclerView)
}