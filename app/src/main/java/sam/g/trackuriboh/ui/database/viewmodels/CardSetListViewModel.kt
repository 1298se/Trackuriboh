package sam.g.trackuriboh.ui.database.viewmodels

import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.repository.CardSetRepository
import javax.inject.Inject

@HiltViewModel
class CardSetListViewModel @Inject constructor(
    private val cardSetRepository: CardSetRepository,
) : BaseSearchViewModel<CardSet>() {

    override fun searchSource(query: String?): Flow<PagingData<CardSet>> = cardSetRepository.search(query)
}
