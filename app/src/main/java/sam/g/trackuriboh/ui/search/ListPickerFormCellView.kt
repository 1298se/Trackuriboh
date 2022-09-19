package sam.g.trackuriboh.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.ListPickerFormCellViewBinding

class ListPickerFormCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {

    val binding = ListPickerFormCellViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setupWith(data: List<String>) {
        // binding.filterOptionsList.adapter = ArrayAdapter(context, android.R.layout.simple_selectable_list_item, data)
    }
}