package sam.g.trackuriboh.ui_card_detail.viewmodels

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
    private val state: SavedStateHandle
) : ViewModel() {
    val cardWithCardSetAndSkuIds: LiveData<ProductWithCardSetAndSkuIds?> = liveData {
        state.get<Long>("cardId")?.let {
            emit(productRepository.getProductWithSkusById(it))
        }
    }
}
