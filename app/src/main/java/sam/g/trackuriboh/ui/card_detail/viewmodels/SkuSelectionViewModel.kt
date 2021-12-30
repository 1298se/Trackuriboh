package sam.g.trackuriboh.ui.card_detail.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.repository.SkuRepository
import javax.inject.Inject

@HiltViewModel
class SkuSelectionViewModel @Inject constructor(
    skuRepository: SkuRepository,
    state: SavedStateHandle,
) : ViewModel() {
    private val productId = state.get<Long>("cardId")!!

    val skus = skuRepository.getSkuIdsForProduct(productId).asLiveData()
}