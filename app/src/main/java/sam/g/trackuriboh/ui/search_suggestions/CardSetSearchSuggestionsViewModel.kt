package sam.g.trackuriboh.ui.search_suggestions

import android.database.Cursor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.repository.CardSetRepository
import javax.inject.Inject

@HiltViewModel
class CardSetSearchSuggestionsViewModel @Inject constructor(
    private val cardSetRepository: CardSetRepository
) : BaseSearchSuggestionsViewModel() {

    override fun getSuggestionsSource(query: String?): Flow<Cursor> =
        cardSetRepository.getSuggestionsCursor(query)
}
