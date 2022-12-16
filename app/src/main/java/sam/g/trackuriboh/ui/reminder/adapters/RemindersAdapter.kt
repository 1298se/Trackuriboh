package sam.g.trackuriboh.ui.reminder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.material.ExperimentalMaterialApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.chauthai.swipereveallayout.ViewBinderHelper
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.data.types.ReminderType
import sam.g.trackuriboh.databinding.ItemReminderBinding
import sam.g.trackuriboh.databinding.ListHeaderBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder
import sam.g.trackuriboh.ui.reminder.RemindersViewModel
import sam.g.trackuriboh.utils.formatDateTime
import java.text.DateFormat

@ExperimentalMaterialApi
class RemindersAdapter : ListAdapter<RemindersViewModel.UiModel, BaseViewHolder<RemindersViewModel.UiModel>>(
    object : DiffUtil.ItemCallback<RemindersViewModel.UiModel>() {
        override fun areItemsTheSame(oldItem: RemindersViewModel.UiModel, newItem: RemindersViewModel.UiModel): Boolean {
           return ((oldItem is RemindersViewModel.UiModel.ReminderItem && newItem is RemindersViewModel.UiModel.ReminderItem && oldItem.reminder.id == newItem.reminder.id) ||
                   oldItem is RemindersViewModel.UiModel.Header && newItem is RemindersViewModel.UiModel.Header && oldItem.title == newItem.title
                   )
        }

        override fun areContentsTheSame(oldItem: RemindersViewModel.UiModel, newItem: RemindersViewModel.UiModel): Boolean {
            return oldItem == newItem
        }
    }
) {
    interface OnItemClickListener {
        fun onItemClick(reminder: Reminder)
        fun onItemEditClick(reminder: Reminder)
        fun onItemDeleteClick(reminder: Reminder)
    }

    inner class HeaderViewHolder(val binding: ListHeaderBinding) : BaseViewHolder<RemindersViewModel.UiModel>(binding.root) {
        override fun bind(item: RemindersViewModel.UiModel) {
            binding.headerStartTextTextview.text = (item as RemindersViewModel.UiModel.Header).title
        }
    }

    inner class ReminderViewHolder(val binding: ItemReminderBinding) : BaseViewHolder<RemindersViewModel.UiModel>(binding.root) {

        init {
            with(binding) {
                itemReminderForeground.setOnClickListener {
                    onItemClickListener?.onItemClick((getItem(bindingAdapterPosition) as RemindersViewModel.UiModel.ReminderItem).reminder)
                }

                itemReminderEditButton.setOnClickListener {
                    onItemClickListener?.onItemEditClick((getItem(bindingAdapterPosition) as RemindersViewModel.UiModel.ReminderItem).reminder)
                }

                itemReminderDeleteButton.setOnClickListener {
                    onItemClickListener?.onItemDeleteClick((getItem(bindingAdapterPosition) as RemindersViewModel.UiModel.ReminderItem).reminder)
                }
            }
        }

        override fun bind(item: RemindersViewModel.UiModel) {
            val reminder = (item as RemindersViewModel.UiModel.ReminderItem).reminder

            val context = itemView.context
            val reminderType = context.getString(reminder.type.resourceId)

            binding.itemReminderTitleTextview.text = if (reminder.host != null) {
                context.getString(R.string.reminder_title, reminder.host, reminderType)
            } else {
                reminderType
            }

            val reminderIcon = when (reminder.type) {
                ReminderType.AUCTION -> AppCompatResources.getDrawable(context, R.drawable.ic_baseline_gavel_24)
                ReminderType.CLAIM_SALE -> AppCompatResources.getDrawable(context, R.drawable.ic_outline_groups_24)
                ReminderType.LISTING -> AppCompatResources.getDrawable(context, R.drawable.ic_outline_assignment_24)
                ReminderType.OTHER -> AppCompatResources.getDrawable(context, R.drawable.ic_baseline_event_note_24)
            }

            binding.itemReminderImage.setImageDrawable(reminderIcon)

            binding.itemReminderDescriptionTextview.text = reminder.date.formatDateTime(DateFormat.MEDIUM, DateFormat.SHORT)
        }
    }

    companion object {
        private const val VIEW_TYPE_REMINDER = 1
        private const val VIEW_TYPE_HEADER = 2
    }

    private var onItemClickListener: OnItemClickListener? = null

    private val viewBinderHelper = ViewBinderHelper()

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RemindersViewModel.UiModel> {
        return when (viewType) {
            VIEW_TYPE_REMINDER -> ReminderViewHolder(ItemReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_HEADER -> HeaderViewHolder(ListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalStateException("invalid viewtype $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<RemindersViewModel.UiModel>, position: Int) {
        if (holder is ReminderViewHolder) {
            viewBinderHelper.bind(
                holder.binding.itemReminderSwipeRevealLayout,
                (getItem(position) as RemindersViewModel.UiModel.ReminderItem).reminder.id.toString()
            )
        }

        holder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RemindersViewModel.UiModel.ReminderItem -> VIEW_TYPE_REMINDER
            is RemindersViewModel.UiModel.Header -> VIEW_TYPE_HEADER
        }
    }
}
