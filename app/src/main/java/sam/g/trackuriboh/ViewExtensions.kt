package sam.g.trackuriboh

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

/**
 * Use this method to add [RecyclerView.ItemDecoration] without stacking behaviour
 * We need to remove the [RecyclerView.ItemDecoration] and add it again
 * because otherwise the item decoration will stack every LiveData callback
 */
fun RecyclerView.addItemDecorationIfExists(decor: RecyclerView.ItemDecoration) {
    removeItemDecoration(decor)
    addItemDecoration(decor)
}

fun TextView.setTextOrHideIfNull(text: String?) {
    if (text == null) {
        visibility = View.GONE
    } else {
        this.text = text
        visibility = View.VISIBLE
    }
}

fun ViewGroup.showOnly(view: View) {
    children.forEach {
        it.visibility = if (it != view) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}

fun ViewGroup.showAllExcept(view: View) {
    children.forEach {
        it.visibility = if (it == view) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}