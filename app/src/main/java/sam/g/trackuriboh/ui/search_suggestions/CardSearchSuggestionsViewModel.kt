package sam.g.trackuriboh.ui.search_suggestions

import android.database.Cursor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class CardSearchSuggestionsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseSearchSuggestionsViewModel() {

    private var setId: Long? = null

    override fun getSuggestionsSource(query: String?): Flow<Cursor> =
        productRepository.getSuggestionsCursorObservable(query, setId)

    fun searchInSet(setId: Long) {
        this.setId = setId
    }
}
