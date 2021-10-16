package tang.song.edu.trackuriboh.ui_database.viewmodels

import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import tang.song.edu.trackuriboh.data.db.entities.Card
import tang.song.edu.trackuriboh.data.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val cardRepository: CardRepository,
) : BaseSearchViewModel<Card>() {

    override fun searchSource(queryString: String?): Flow<PagingData<Card>> = cardRepository.getSearchResultStream(queryString)

}
