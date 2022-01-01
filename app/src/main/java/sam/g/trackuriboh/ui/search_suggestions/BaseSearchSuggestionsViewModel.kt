package sam.g.trackuriboh.ui.search_suggestions

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

abstract class BaseSearchSuggestionsViewModel : ViewModel() {

    private val suggestionsQuery = MutableLiveData<String?>(null)

    val suggestionsCursor = Transformations.switchMap(suggestionsQuery) {
        getSuggestionsSource(it).asLiveData()
    }

    fun getSuggestions(query: String?) {
        suggestionsQuery.value = query
    }

    protected abstract fun getSuggestionsSource(query: String?): Flow<Cursor>

}
