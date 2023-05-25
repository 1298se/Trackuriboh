package sam.g.trackuriboh.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import sam.g.trackuriboh.R

abstract class SwipeToDeleteCallback(
    private val context: Context
) : ItemTouchHelper.Callback() {
    private val background =
        ColorDrawable(MaterialColors.getColor(context, R.attr.colorError, Color.RED))
    private val icon = ResourcesCompat.getDrawable(
        context.resources,
        R.drawable.ic_baseline_delete_forever_24,
        context.theme
    )!!

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView: View = viewHolder.itemView


        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top, itemView.right, itemView.bottom
        )

        background.draw(c)

        val iconMargin =
            context.resources.getDimensionPixelOffset(R.dimen.list_item_padding_horizontal)
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
        val iconRight: Int = itemView.right - iconMargin

        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon.draw(c)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }
}
