package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.network.PagedListBoundaryCallbackResponse
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardListViewModel @ViewModelInject constructor(
    private val cardRepository: CardRepository,
) : BaseSearchViewModel<Card>() {

    val cardList: LiveData<PagedList<Card>> = itemList

    override suspend fun searchSource(queryString: String): PagedListBoundaryCallbackResponse<Card> = cardRepository.search(queryString)

    override suspend fun totalListSource(): PagedListBoundaryCallbackResponse<Card> = cardRepository.getCardList()

}
