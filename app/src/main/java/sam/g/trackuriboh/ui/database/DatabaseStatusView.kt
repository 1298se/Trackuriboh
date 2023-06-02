package sam.g.trackuriboh.ui.database

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.DatabaseStatusViewBinding
import sam.g.trackuriboh.ui.database.viewmodels.DatabaseExploreViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {

    private val binding =
        DatabaseStatusViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setupWith(data: DatabaseExploreViewModel.DatabaseStatusUiState) {
        binding.lastUpdatedTextview.text =
            data.lastUpdated?.let { SimpleDateFormat("MMM d, yyy", Locale.getDefault()).format(it) }
        binding.totalCardCountTextview.text = data.totalCardCount?.toString()
        binding.totalCardSetCountTextview.text = data.totalCardSetCount?.toString()


    }

    fun setupUpdateButtonState(buttonState: DatabaseExploreViewModel.UpdateButtonUiState) {
        binding.updateButton.isEnabled = buttonState.enabled

        when (val status = buttonState.status) {
            DatabaseExploreViewModel.ButtonStatus.UpToDate -> {
                with(binding.updateButton) {
                    text = resources.getString(R.string.lbl_up_to_date)
                    isEnabled = false
                }
            }

            is DatabaseExploreViewModel.ButtonStatus.UpdateAvailable -> {
                with(binding.updateButton) {
                    setOnClickListener {
                        status.onUpdateButtonClick()
                    }
                    text = resources.getString(R.string.lbl_update)
                }
            }
        }
    }
}