package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.repository.CardRepository

class CardListViewModel @ViewModelInject constructor(
    private val cardRepository: CardRepository,
) : BaseSearchViewModel<Card>() {

    override fun searchSource(queryString: String?): Flow<PagingData<Card>> = cardRepository.getSearchResultStream(queryString)

}
