package sam.g.trackuriboh.ui.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class VerticalSpaceItemDecoration(private val spacing: Float) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != (parent.adapter?.itemCount?.minus(1)
                ?: Integer.MAX_VALUE)
        ) {
            outRect.bottom = spacing.toInt()
        }
    }
}
