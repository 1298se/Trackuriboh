package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.network.PagedListBoundaryCallbackResponse
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository

class CardSetViewModel(private val cardSetRepository: CardSetRepository) : BaseSearchViewModel<CardSet>() {
    val cardSetList: LiveData<PagedList<CardSet>> = itemList

    override suspend fun searchSource(queryString: String): PagedListBoundaryCallbackResponse<CardSet> = cardSetRepository.search(queryString)
}
