package sam.g.trackuriboh.data.db.dao

import android.app.SearchManager
import android.database.MatrixCursor
import android.provider.BaseColumns

const val SEARCH_SUGGESTIONS_RESULT_SIZE = 5

fun getFuzzySearchQuery(query: String?) = "%${(query ?: "").replace(' ', '%')}%"

fun List<String>.toSearchSuggestionsCursor(): MatrixCursor {
    return MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1)).apply {
            forEachIndexed { index, s ->
            addRow(arrayOf(index, s))
        }
    }
}
