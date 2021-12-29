package sam.g.trackuriboh.ui.database.adapters

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cursoradapter.widget.CursorAdapter
import com.google.android.material.textview.MaterialTextView
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.ItemSearchSuggestionBinding

class SearchSuggestionsAdapter(
    context: Context?,
    cursor: Cursor?
) : CursorAdapter(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return ItemSearchSuggestionBinding.inflate(LayoutInflater.from(context), parent, false).root
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        view.findViewById<MaterialTextView>(R.id.search_suggestion_textview).text = cursor.getString(
            cursor.getColumnIndexOrThrow(android.app.SearchManager.SUGGEST_COLUMN_TEXT_1)
        )
    }
}