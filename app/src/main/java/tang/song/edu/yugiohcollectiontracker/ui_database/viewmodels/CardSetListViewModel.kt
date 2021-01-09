package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository

class CardSetListViewModel @ViewModelInject constructor(
    private val cardSetRepository: CardSetRepository,
) : BaseSearchViewModel<CardSet>() {

    override fun searchSource(queryString: String?): Flow<PagingData<CardSet>> = cardSetRepository.search(queryString)
}
