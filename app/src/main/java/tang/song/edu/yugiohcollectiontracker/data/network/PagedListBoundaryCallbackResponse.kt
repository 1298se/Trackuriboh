package tang.song.edu.yugiohcollectiontracker.data.network

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PagedListBoundaryCallbackResponse<T>(
    val data: LiveData<PagedList<T>>,
    val networkErrors: LiveData<String>
)
