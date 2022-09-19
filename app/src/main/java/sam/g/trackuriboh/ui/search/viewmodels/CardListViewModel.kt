package sam.g.trackuriboh.ui.search.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.ui.search.CardListFragment
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    state: SavedStateHandle
) : ViewModel() {

    sealed class UiModel {
        data class CardItem(val productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds) :
            UiModel()

        object FilterHeaderItem : UiModel()
    }

    private val setId = state.get<Long?>(CardListFragment.ARG_SET_ID)
    private val query = state.get<String?>(CardListFragment.ARG_QUERY)

    val searchResult = getSearchResultStream().cachedIn(viewModelScope).asLiveData()

    private fun getSearchResultStream(): Flow<PagingData<UiModel>> {
        val resultStream: Flow<PagingData<UiModel>> = if (setId != null) {
            productRepository.getSearchResultStreamInSet(setId, query)
        } else {
            productRepository.getSearchResultStream(query)
        }.map { pagingData -> pagingData.map { UiModel.CardItem(it) } }

        return resultStream.map { it.insertHeaderItem(item = UiModel.FilterHeaderItem) }
    }
}
