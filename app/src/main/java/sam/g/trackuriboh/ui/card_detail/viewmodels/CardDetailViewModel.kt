package sam.g.trackuriboh.ui.card_detail.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    state: SavedStateHandle
) : ViewModel() {

    enum class Page(val position: Int) {
        OVERVIEW(0),
        PRICES(1),
    }

    // TODO: Fix magic strings
    private val cardId = state.get<Long>("cardId")!!

    val cardWithCardSetAndSkuIds: LiveData<ProductWithCardSetAndSkuIds> = liveData {
        emit(productRepository.getProductWithSkusById(cardId))
    }
}
