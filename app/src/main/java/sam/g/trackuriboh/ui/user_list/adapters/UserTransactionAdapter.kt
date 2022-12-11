package sam.g.trackuriboh.ui.user_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.g.trackuriboh.databinding.ItemUserTransactionBinding
import sam.g.trackuriboh.databinding.UserTransactionsHeaderBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder
import sam.g.trackuriboh.ui.user_list.UserListEntryDetailViewModel

class UserTransactionAdapter(private val onItemClickListener: OnInteractionListener)
    : ListAdapter<UserListEntryDetailViewModel.UiModel, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<UserListEntryDetailViewModel.UiModel>() {
        override fun areItemsTheSame(
            oldItem: UserListEntryDetailViewModel.UiModel,
            newItem: UserListEntryDetailViewModel.UiModel
        ): Boolean {
            return (oldItem is UserListEntryDetailViewModel.UiModel.Header && newItem is UserListEntryDetailViewModel.UiModel.Header) ||
                    (oldItem is UserListEntryDetailViewModel.UiModel.TransactionItem && newItem is UserListEntryDetailViewModel.UiModel.TransactionItem &&
                    oldItem.transaction.id == newItem.transaction.id)
        }

        override fun areContentsTheSame(
            oldItem: UserListEntryDetailViewModel.UiModel,
            newItem: UserListEntryDetailViewModel.UiModel
        ): Boolean {
            return oldItem == newItem
        }

    }
) {
    companion object {
        private const val VIEW_TYPE_TRANSACTION = 1
        private const val VIEW_TYPE_HEADER = 2
    }

    interface OnInteractionListener {
        fun onAddNewTransactionClick()
    }

    inner class UserTransactionViewHolder(
        private val binding: ItemUserTransactionBinding
    ) : BaseViewHolder<UserListEntryDetailViewModel.UiModel>(binding.root) {

        override fun bind(item: UserListEntryDetailViewModel.UiModel) {
            val transaction = (item as UserListEntryDetailViewModel.UiModel.TransactionItem).transaction

            binding.itemUserTransactionTime.text = transaction.date.toString()
            binding.itemUserTransactionType.text = transaction.type.name
            binding.itemUserTransactionAmount.text = transaction.price.toString()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UserListEntryDetailViewModel.UiModel.TransactionItem -> VIEW_TYPE_TRANSACTION
            UserListEntryDetailViewModel.UiModel.Header -> VIEW_TYPE_HEADER
        }
    }

    inner class UserTransactionHeaderViewHolder(
        binding: UserTransactionsHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.addTransactionButton.setOnClickListener {
                onItemClickListener.onAddNewTransactionClick()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TRANSACTION -> UserTransactionViewHolder(ItemUserTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_HEADER -> UserTransactionHeaderViewHolder(UserTransactionsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalStateException("invalid viewtype $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserTransactionViewHolder) {
            holder.bind(getItem(position))
        }
    }
}
