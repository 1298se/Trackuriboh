package sam.g.trackuriboh.ui.card_set_detail.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.repository.CardSetRepository
import javax.inject.Inject

@HiltViewModel
class CardSetDetailViewModel @Inject constructor(
    private val cardSetRepository: CardSetRepository,
    state: SavedStateHandle
) : ViewModel() {

    var query: String? = null

    // TODO: Fix Magic Strings
    private val setId = state.get<Long>("setId")!!

    val cardSet: LiveData<CardSet?> = liveData {
        emit(cardSetRepository.getCardSet(setId))
    }
}
