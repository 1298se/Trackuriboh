package sam.g.trackuriboh.ui.search.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.ui.search.CardSetListFragment
import javax.inject.Inject

@HiltViewModel
class CardSetListViewModel @Inject constructor(
    cardSetRepository: CardSetRepository,
    state: SavedStateHandle
) : ViewModel() {

    val searchResult =
        cardSetRepository.search(state[CardSetListFragment.ARG_QUERY]).cachedIn(viewModelScope)
            .asLiveData(viewModelScope.coroutineContext)
}
