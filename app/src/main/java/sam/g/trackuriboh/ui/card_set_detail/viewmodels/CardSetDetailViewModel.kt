package sam.g.trackuriboh.ui.card_set_detail.viewmodels

import android.database.Cursor
import androidx.lifecycle.*
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.ui.database.viewmodels.BaseSearchViewModel
import javax.inject.Inject

@HiltViewModel
class CardSetDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cardSetRepository: CardSetRepository,
    state: SavedStateHandle
) : BaseSearchViewModel<ProductWithCardSetAndSkuIds>() {

    val searchSuggestionsCursor: LiveData<Cursor>
        get() = _searchSuggestionsCursor

    private val _searchSuggestionsCursor = MediatorLiveData<Cursor>()

    // TODO: Fix Magic Strings
    private val setId = state.get<Long>("setId")!!

    override fun searchSource(query: String?): Flow<PagingData<ProductWithCardSetAndSkuIds>> =
        productRepository.getSearchResultStreamInSet(setId, query)

    val cardSet: LiveData<CardSet> = liveData {
        emit(cardSetRepository.getCardSet(setId))
    }

    fun setSearchSuggestion(query: String?) {
        viewModelScope.launch {
            _searchSuggestionsCursor.value = productRepository.getSuggestionsCursorInSet(setId, query)
        }
    }
}
