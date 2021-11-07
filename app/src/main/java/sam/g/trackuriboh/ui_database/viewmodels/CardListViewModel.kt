package sam.g.trackuriboh.ui_database.viewmodels

import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.relations.ProductWithSetInfo
import sam.g.trackuriboh.data.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val cardRepository: CardRepository,
) : BaseSearchViewModel<ProductWithSetInfo>() {

    override fun searchSource(query: String?): Flow<PagingData<ProductWithSetInfo>> =
        cardRepository.getSearchResultStream(query)

}
