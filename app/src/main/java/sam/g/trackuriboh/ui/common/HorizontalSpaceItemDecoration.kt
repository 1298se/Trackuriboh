package sam.g.trackuriboh.ui.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpaceItemDecoration(private val spacing: Float) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1) ?: Integer.MAX_VALUE) {
            outRect.right = spacing.toInt()
        }
    }
}