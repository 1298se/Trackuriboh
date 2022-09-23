package sam.g.trackuriboh.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.PersistentSearchViewBinding

class ToolbarSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {
    private val binding = PersistentSearchViewBinding.inflate(LayoutInflater.from(context), this, true)

    val searchView = binding.searchView

    override fun clearFocus() {
        binding.searchView.clearFocus()
        binding.focusDummyView.requestFocus()
    }
}