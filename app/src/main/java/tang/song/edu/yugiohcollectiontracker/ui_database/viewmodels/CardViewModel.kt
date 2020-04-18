package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.network.PagedListBoundaryCallbackResponse
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardViewModel(private val cardRepository: CardRepository) : BaseSearchViewModel<Card>() {

    val cardList: LiveData<PagedList<Card>> = itemList

    override suspend fun totalListSource(): PagedListBoundaryCallbackResponse<Card> = cardRepository.getCardList()
    override suspend fun searchSource(queryString: String): PagedListBoundaryCallbackResponse<Card> = cardRepository.search(queryString)
}
