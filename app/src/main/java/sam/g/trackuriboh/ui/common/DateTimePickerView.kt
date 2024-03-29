package sam.g.trackuriboh.ui.common

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
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.DateTimePickerBinding
import sam.g.trackuriboh.managers.TimeZoneManager
import java.util.*
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class DateTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {

    @Inject
    lateinit var timeZoneManager: TimeZoneManager

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
            val timeZoneResourceIds = timeZoneManager.getTimeZoneResourceValues()

            val timeZoneStrings = timeZoneResourceIds.map { stringResource(id = it) }

            var selectedTimeZoneIndex by rememberSaveable { mutableStateOf(0) }

            MdcTheme {
                Column {
                    val selectedTimeZone = timeZoneManager.getTimeZoneFromResourceValue(timeZoneResourceIds[selectedTimeZoneIndex])

                    AppThemeDenseOutlinedAutoCompleteTextField(
                        options = timeZoneStrings,
                        selectedOption = timeZoneStrings[selectedTimeZoneIndex],
                        onOptionSelected = { position -> selectedTimeZoneIndex = position}
                    )

                    val daylightSavingsActive = selectedTimeZone.inDaylightTime(Date())

                    Text(
                        text = stringResource(id = if (daylightSavingsActive) {
                            R.string.timezone_daylight_savings_active
                        } else {
                            R.string.timezone_standard_time_active
                        }),
                        style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onSurface)
                    )

                    AppThemeDialogButtons(
                        positiveButtonEnabled = true,
                        negativeButtonEnabled = true,
                        positiveButtonText = stringResource(id = R.string.lbl_ok).uppercase(),
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
                                    timeZone = selectedTimeZone
                                }
                            )
                        },
                        onNegativeButtonClick = {
                            with (binding.dateTimeTabLayout) {
                                selectTab(getTabAt(0))
                            }
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