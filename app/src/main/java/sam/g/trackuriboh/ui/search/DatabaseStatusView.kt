package sam.g.trackuriboh.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.DatabaseStatusViewBinding
import java.text.SimpleDateFormat
import java.util.*

class DatabaseStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {

    data class DatabaseStatusInfo(
        val lastUpdated: Date,
        val totalCardSetCount: Int,
        val totalCardCount: Int,
    )

    private val binding =
        DatabaseStatusViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setupWith(data: DatabaseStatusInfo) {
        binding.lastUpdatedTextview.text = SimpleDateFormat("MMM d, yyy", Locale.getDefault()).format(data.lastUpdated)
        binding.totalCardCountTextview.text = data.totalCardCount.toString()
        binding.totalCardSetCountTextview.text = data.totalCardSetCount.toString()
    }
}