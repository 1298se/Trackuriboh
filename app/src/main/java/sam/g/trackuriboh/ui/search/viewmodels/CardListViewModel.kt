package sam.g.trackuriboh.ui.search.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
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
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.cache.ProductLocalCache
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.ui.search.CardListFragment
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    state: SavedStateHandle
) : ViewModel() {

    enum class SortOption {
        BEST_MATCH,
        PRICE_LOW_TO_HIGH,
        PRICE_HIGH_TO_LOW;

        fun getDisplayStringResId() =
            when (this) {
                BEST_MATCH -> R.string.lbl_best_match
                PRICE_LOW_TO_HIGH -> R.string.lbl_price_low_to_high
                PRICE_HIGH_TO_LOW -> R.string.lbl_price_high_to_low
            }
    }

    sealed class UiState {
        data class CardItemUiState(val productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds) :
            UiState()

        data class SortFilterUiState(
            val currentSortOrdering: SortOption = SortOption.BEST_MATCH,
            val sortOptions: List<SortOption> = SortOption.values().toList()
        ) : UiState()
    }

    private val setId = state.get<Long?>(CardListFragment.ARG_SET_ID)
    private val query = state.get<String?>(CardListFragment.ARG_QUERY)

    private val sortOrdering = MutableLiveData(SortOption.BEST_MATCH)

    val searchResult = Transformations.switchMap(sortOrdering) {
        getSearchResultStream(it).cachedIn(viewModelScope).asLiveData()
    }

    private fun getSearchResultStream(sortOption: SortOption): Flow<PagingData<UiState>> {
        val (sortOrdering, sortDirection) = when (sortOption) {
            SortOption.BEST_MATCH -> ProductLocalCache.ProductSortOrdering.BEST_MATCH to ProductLocalCache.SortDirection.ASC
            SortOption.PRICE_LOW_TO_HIGH -> ProductLocalCache.ProductSortOrdering.PRICE to ProductLocalCache.SortDirection.ASC
            SortOption.PRICE_HIGH_TO_LOW -> ProductLocalCache.ProductSortOrdering.PRICE to ProductLocalCache.SortDirection.DESC
        }

        val resultStream: Flow<PagingData<UiState>> =
            productRepository.getSearchResultStream(query, setId, sortOrdering, sortDirection)
                .map { pagingData -> pagingData.map { UiState.CardItemUiState(it) } }

        return resultStream.map {
            it.insertHeaderItem(
                item = UiState.SortFilterUiState(
                    currentSortOrdering = sortOption
                )
            )
        }
    }

    fun setSortOrdering(sortOption: SortOption) {
        sortOrdering.value = sortOption
    }
}
