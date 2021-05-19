package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository
import javax.inject.Inject

@HiltViewModel
class CardSetListViewModel @Inject constructor(
    private val cardSetRepository: CardSetRepository,
) : BaseSearchViewModel<CardSet>() {

    override fun searchSource(queryString: String?): Flow<PagingData<CardSet>> = cardSetRepository.search(queryString)
}
