package sam.g.trackuriboh.ui.database

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.DatabaseStatusViewBinding
import sam.g.trackuriboh.utils.show
import java.text.SimpleDateFormat
import java.util.*

class DatabaseStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {

    sealed class ButtonState {
        data class UpdateAvailable(val onUpdateButtonClick: () -> Unit) :
            ButtonState()

        object UpToDate : ButtonState()
    }

    data class UiState(
        val lastUpdated: Date? = null,
        val totalCardSetCount: Int? = null,
        val totalCardCount: Int? = null,
    )

    private val binding =
        DatabaseStatusViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setupWith(data: UiState) {
        binding.lastUpdatedTextview.text =
            data.lastUpdated?.let { SimpleDateFormat("MMM d, yyy", Locale.getDefault()).format(it) }
        binding.totalCardCountTextview.text = data.totalCardCount?.toString()
        binding.totalCardSetCountTextview.text = data.totalCardSetCount?.toString()


    }

    fun setupUpdateButtonState(buttonState: ButtonState) {
        when (buttonState) {
            ButtonState.UpToDate -> {
                with(binding.updateButton) {
                    show(true)
                    isEnabled = false
                    text = resources.getString(R.string.lbl_up_to_date)
                }
            }
            is ButtonState.UpdateAvailable -> {
                with(binding.updateButton) {
                    setOnClickListener {
                        buttonState.onUpdateButtonClick()
                    }
                    show(true)
                    isEnabled = true
                    text = resources.getString(R.string.lbl_update)
                }
            }
        }
    }
}