package sam.g.trackuriboh.ui.search.viewmodels

import android.os.Parcelable
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.CardRarity
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.ui.search.CardFilterBottomSheetFragment
import javax.inject.Inject

@HiltViewModel
class CardFilterViewModel @Inject constructor(
    productRepository: ProductRepository,
    state: SavedStateHandle,
) : ViewModel() {

    @Parcelize
    data class FilterUiModel(
        val rarities: List<CardRarity>,
        val cardTypes: List<CardTypeFilterOption>,
    ) : Parcelable

    enum class CardTypeFilterOption(val displayName: String) {
        MAIN_DECK_MONSTER("Main Deck Monster"),
        EXTRA_DECK_MONSTER("Extra Deck Monster"),
        SPELL("Spell"),
        TRAP("Trap")
    }

    val allFilters = liveData {
        val rarities = productRepository.getRarities(state.get<String>(CardFilterBottomSheetFragment.ARG_QUERY))

        emit(FilterUiModel(rarities.map { it }, CardTypeFilterOption.values().toList()))
    }

    val selectedFilters: LiveData<FilterUiModel>
        get() = _selectedFilters

    private val _selectedFilters = MutableLiveData<FilterUiModel>().apply {
        value = FilterUiModel(rarities = emptyList(), cardTypes = emptyList())
    }
}