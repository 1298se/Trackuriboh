package sam.g.trackuriboh

import android.view.View
import android.widget.TextView

fun TextView.setTextOrHideIfNull(text: String?) {
    if (text == null) {
        visibility = View.GONE
    } else {
        this.text = text
        visibility = View.VISIBLE
    }
}