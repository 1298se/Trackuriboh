package sam.g.trackuriboh.ui_card_set_detail.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.repository.CardRepository
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.ui_database.viewmodels.BaseSearchViewModel
import javax.inject.Inject

@HiltViewModel
class CardSetDetailViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val cardSetRepository: CardSetRepository,
    private val state: SavedStateHandle
) : BaseSearchViewModel<ProductWithCardSetAndSkuIds>() {

    private val setId = state.get<Long>("setId")
    override fun searchSource(query: String?): Flow<PagingData<ProductWithCardSetAndSkuIds>> =
        cardRepository.getSearchResultStreamInSet(setId, query)

    val cardSet: LiveData<CardSet> = liveData {
        setId?.let {
            emit(cardSetRepository.getCardSet(setId))
        }
    }
}
