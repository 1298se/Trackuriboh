package sam.g.trackuriboh.ui_common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.tabs.TabLayout
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.DateTimePickerBinding
import java.util.*

@ExperimentalMaterialApi
class DateTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {

    interface OnInteractionListener {
        fun onCancelClick()
        fun onSaveClick(calendar: Calendar)
    }

    private var listener: OnInteractionListener? = null

    private val binding = DateTimePickerBinding.inflate(LayoutInflater.from(context), this)

    init {
        minimumHeight = resources.getDimension(R.dimen.date_time_picker_min_height).toInt()
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        initTabLayout()
        initPickers()
        initComposeContainer()
    }

    private fun initPickers() {
        with(binding.datePicker) {
            init(year, month, dayOfMonth) { _, _, _, _ ->
                binding.dateTimeTabLayout.getTabAt(1)?.select()
            }
        }
    }

    private fun initTabLayout() {
        binding.dateTimeTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.datePicker.visibility = VISIBLE
                        binding.timePicker.visibility = GONE
                    }
                    1 -> {
                        binding.datePicker.visibility = GONE
                        binding.timePicker.visibility = VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }

    private fun initComposeContainer() {
        binding.datePickerTimezoneComposeContainer.setContent {
            val timeZoneKeys = stringArrayResource(id = R.array.time_zone_keys)
            val timeZoneValues = stringArrayResource(id = R.array.time_zone_values)

            var selectedTimeZoneIndex by rememberSaveable { mutableStateOf(0) }

            MdcTheme {
                Column {
                    AppThemeDenseOutlinedAutoCompleteTextField(
                        options = timeZoneKeys.toList(),
                        selectedOption = timeZoneKeys[selectedTimeZoneIndex],
                        onOptionSelected = { position -> selectedTimeZoneIndex = position}
                    )

                    val daylightSavingsActive = TimeZone.getDefault().inDaylightTime(Date())


                   Text(text = "Daylight Savings is currently active", style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onSurface))


                    AppThemeDialogButtons(
                        positiveButtonEnabled = true,
                        negativeButtonEnabled = true,
                        positiveButtonText = stringResource(id = R.string.lbl_save).uppercase(),
                        negativeButtonText = stringResource(id = R.string.lbl_cancel).uppercase(),
                        onPositiveButtonClick = {
                            listener?.onSaveClick(
                                Calendar.getInstance().apply {
                                    set(
                                        binding.datePicker.year,
                                        binding.datePicker.month,
                                        binding.datePicker.dayOfMonth,
                                        binding.timePicker.hour,
                                        binding.timePicker.minute
                                    )
                                    timeZone = TimeZone.getTimeZone(timeZoneValues[selectedTimeZoneIndex])
                                }
                            )
                        },
                        onNegativeButtonClick = {
                            listener?.onCancelClick()
                        }
                    )
                }

            }
        }
    }

    fun setOnInteractionListener(listener: OnInteractionListener) {
        this.listener = listener
    }
}