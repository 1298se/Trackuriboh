package sam.g.trackuriboh.ui_card_set_detail.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class CardSetDetailViewModel @Inject constructor(
    private val cardRepository: CardRepository,
) : ViewModel() {
    var currentProductListResult: Flow<PagingData<Product>>? = null

}