package sam.g.trackuriboh.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.ChipFormCellViewBinding

class ChipFormCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {
    private var isSingleLine = false
    private var chipGroup: ChipGroup

    val binding = ChipFormCellViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ChipFormCellView,
            0, 0).apply {

            try {
                isSingleLine = getBoolean(R.styleable.ChipFormCellView_singleLine, false)

                chipGroup = if (isSingleLine) binding.singleLineChipgroup else binding.multiLineChipgroup
            } finally {
                recycle()
            }
        }
    }

    fun setupWith(chipTitles: List<String>) {
        for (title in chipTitles) {
            with (chipGroup) {
                addView(Chip(context).apply {
                    text = title
                    isCheckable = true
                })
            }
        }
    }
}