package tang.song.edu.trackuriboh.ui_card_set_detail.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import tang.song.edu.trackuriboh.data.db.entities.Card
import tang.song.edu.trackuriboh.data.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class CardSetDetailViewModel @Inject constructor(
    private val cardRepository: CardRepository,
) : ViewModel() {
    var currentCardListResult: Flow<PagingData<Card>>? = null

}