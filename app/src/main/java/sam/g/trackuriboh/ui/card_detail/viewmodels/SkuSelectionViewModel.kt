package sam.g.trackuriboh.ui.card_detail.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.repository.SkuRepository
import sam.g.trackuriboh.ui.card_detail.SkuSelectionBottomSheetFragment
import javax.inject.Inject

@HiltViewModel
class SkuSelectionViewModel @Inject constructor(
    skuRepository: SkuRepository,
    state: SavedStateHandle,
) : ViewModel() {
    private val cardId = state.get<Long>(SkuSelectionBottomSheetFragment.ARG_CARD_ID)!!

    val skus = liveData {
        emit(skuRepository.getSkusWithConditionAndPrinting(cardId))
    }
}