package sam.g.trackuriboh.ui_common

import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.recyclerview.widget.RecyclerView

class DateTimePickerAdapter : RecyclerView.Adapter<DateTimePickerAdapter.DateTimePickerViewHolder>() {
    interface OnDateTimeSelectedListener : DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener

    private var listener: OnDateTimeSelectedListener? = null

    abstract inner class DateTimePickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind()
    }

    inner class DatePickerViewHolder(
        datePicker: DatePicker,
    ) : DateTimePickerViewHolder(datePicker) {
        init {
            with(datePicker) {
                init(year, month, dayOfMonth) { _, year, month, dayOfMonth ->
                    listener?.onDateChanged(datePicker, year, month, dayOfMonth)
                }
            }
        }

        override fun bind() {}
    }

    inner class TimePickerViewHolder(
        timePicker: TimePicker,
    ) : DateTimePickerViewHolder(timePicker) {
        init {
            with(timePicker) {
                setOnTimeChangedListener { _, hour, minute ->
                    listener?.onTimeChanged(timePicker, hour, minute)
                }
            }
        }

        override fun bind() {}
    }

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateTimePickerViewHolder {
        return when (viewType) {
            0 -> DatePickerViewHolder(
                DatePicker(parent.context).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                }
            )
            1 -> TimePickerViewHolder(
                TimePicker(parent.context).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                })
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: DateTimePickerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 2

    fun setOnInteractionListener(listener: OnDateTimeSelectedListener) {
        this.listener = listener
    }
}