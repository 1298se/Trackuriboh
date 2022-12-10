package sam.g.trackuriboh.ui.user_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sam.g.trackuriboh.data.db.entities.UserTransaction
import sam.g.trackuriboh.databinding.ItemUserTransactionBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder

class UserTransactionAdapter : ListAdapter<UserTransaction, BaseViewHolder<UserTransaction>>(
    object : DiffUtil.ItemCallback<UserTransaction>() {
        override fun areItemsTheSame(oldItem: UserTransaction, newItem: UserTransaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserTransaction,
            newItem: UserTransaction
        ): Boolean {
            return oldItem == newItem
        }
    }) {

    inner class UserTransactionViewHolder(
        private val binding: ItemUserTransactionBinding
    ) : BaseViewHolder<UserTransaction>(binding.root) {

        override fun bind(item: UserTransaction) {
            binding.itemUserTransactionTime.text = item.date.toString()
            binding.itemUserTransactionType.text = item.type.name
            binding.itemUserTransactionAmount.text = item.price.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<UserTransaction> {
        return UserTransactionViewHolder(ItemUserTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<UserTransaction>, position: Int) {
        holder.bind(getItem(position))
    }
}