package sam.g.trackuriboh.ui.database.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    state: SavedStateHandle
) : BaseSearchViewModel<ProductWithCardSetAndSkuIds>(state.get("query")) {

    override fun searchSource(query: String?): Flow<PagingData<ProductWithCardSetAndSkuIds>> =
        productRepository.getSearchResultStream(query)

}
