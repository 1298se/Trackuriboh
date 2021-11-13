package sam.g.trackuriboh.ui_card_detail.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.db.relations.ProductWithSetAndSkuIds
import sam.g.trackuriboh.data.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    val cardWithSetAndSkuIds: LiveData<ProductWithSetAndSkuIds?> by lazy {
        liveData {
            state.get<Long>("cardId")?.let {
                emit(cardRepository.getCardWithSkusById(it))
            }
        }
    }
}
