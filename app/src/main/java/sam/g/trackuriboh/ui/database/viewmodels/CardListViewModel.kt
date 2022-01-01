package sam.g.trackuriboh.ui.database.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.ui.database.CardListFragment.Companion.ARG_SET_ID
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    state: SavedStateHandle
) : BaseSearchViewModel<ProductWithCardSetAndSkuIds>() {

    private val setId = state.get<Long?>(ARG_SET_ID)

    override fun searchSource(query: String?): Flow<PagingData<ProductWithCardSetAndSkuIds>> {
        return if (setId != null) {
            productRepository.getSearchResultStreamInSet(setId, query)
        } else {
            productRepository.getSearchResultStream(query)
        }
    }
}
